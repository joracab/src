/*
  Id3 metric : Id3 based on metric split condtions.
  This id3 woks on nominal, numerical, and structured attributes.
  Th information gain is used as splitting criterion.
*/
package racs;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation.*;
import weka.classifiers.trees.j48.*;
import weka.core.*;
import weka.classifiers.trees.j48.C45PruneableClassifierTree;
import java.io.*;
import java.util.*;


public class Id3Metric extends Classifier implements Drawable 
{
  public Id3Metric() {
	  m_id = ms_count++;
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  /** the node's id */
  private int m_id;

  /** static count to assign the ids */
  private static int ms_count = 0;
  
  private int m;                      // maximum number of branches
  private int [] attributes_method;   // median or center or any another thecnique for computing the prototype
                                       /*
                                         0 Median
                                         1 Center
                                         2 ...
                                       */
  private Id3Metric[] m_Successors;   // The node's successors. (WEKA attribute)
  private Attribute m_Attribute;      // Attribute used for splitting. (WEKA attribute)
  private double [] m_att_prototypes; // Attribute prototypes used by the split
  private double[] m_Distribution;    // Class distribution if node is leaf. (WEKA attribute)
  private int [] weight_prototypes;   // weight of each prototype = number of instances acquired by the prototype
  private double m_ClassValue;        // Class value if node is leaf. (WEKA attribute)
  private Attribute m_ClassAttribute; // Class attribute of dataset. (WEKA attribute)
  private boolean IG_splitting;       // Split a node using the Information Gain criterion.  (1)
  private boolean GR_splitting;       // Split a node using the Gain Ratio criterion.        (2)
  private MetricSpace mms;

 //private  double [] ratio_prototypes;

 /*
  Null constructor method. (REQUIRED !!)
 */
 public void buildClassifier(Instances data){};

 /**
   Constructor: set of instances + metric space + maximum number of children
 */
 public void buildClassifier(Instances data, MetricSpace ms, int max_branches, int split_criterion, int [] attr_method) throws Exception
 {
   Enumeration enumAtt, enume;

   m=max_branches;
   attributes_method=attr_method;
   mms=ms;
   IG_splitting = false; 
   GR_splitting = false;
   
   boolean m_pruneTheTree = false;
   
   int numIns=data.numInstances();

   if (split_criterion==0) IG_splitting = true;
   else if(split_criterion==1) GR_splitting=true;

   if (data.classAttribute().isString())
   {
	   
	 //  View.viewData(data);
	// System.out.println("nohay atributos STring");
	   
   }
   // checking nominal CLASS value and missing values conditions
   if (!data.classAttribute().isNominal())
      throw new UnsupportedClassTypeException("Id3: nominal class, please.");
   //System.out.println(data.numAttributes());
  for (int i=0;i<data.numAttributes()-1;i++) data.deleteWithMissing(i);
       //System.out.println("hheeeee");
   int numatt=data.numAttributes();
   enumAtt = data.enumerateAttributes();
   
    numIns=data.numInstances();
   while (enumAtt.hasMoreElements())
   {
     Attribute attr = (Attribute) enumAtt.nextElement();
     enume = data.enumerateInstances();
     while (enume.hasMoreElements())
       if (((Instance) enume.nextElement()).isMissing(attr))
         throw new NoSupportForMissingValuesException("Id3: no missing values, please.");
   }
   data = new Instances(data);
   data.deleteWithMissingClass();
   boolean has_string=data.checkForStringAttributes();
   
   System.out.println("data"+data.numInstances());
   
   
   //System.out.println(data.toString());
   makeTree(data,ms,split_criterion); // inferring the decision tree (make a tree using the metric space <ms> and the splitting criterion) !!
 };

 /**
   Inferring metric decision tree.
 */
 private void makeTree(Instances data, MetricSpace ms, int split_criterion) throws Exception
 {
   double [] heuristics; //infoGains,gainRatios;
   Instances[] splitData;
   Enumeration attEnum, instEnum;
   Vector classes;
   double extracted_class;
   int frequency,more_frequent_class;

   if (data.numInstances() == 0)
   {
     m_Attribute = null;
     m_ClassValue = Instance.missingValue();
     m_Distribution = new double[data.numClasses()];
     return;
   }

   /*infoGains  = new double[data.numAttributes()]; // IG provided by each attribute
   gainRatios = new double[data.numAttributes()]; // GR provided by each attribute*/
   heuristics = new double[data.numAttributes()];
   attEnum = data.enumerateAttributes();

 System.out.println("numero de instancias"+ data.numInstances());
   
   int counter1=0;
   while (attEnum.hasMoreElements())
   {

	   
	 
	   
//     System.out.println("instancias "+counter1);
     Attribute att = (Attribute) attEnum.nextElement(); // checking the attribute
    
     
   // System.out.println(att.name());

     if (IG_splitting) heuristics[att.index()] = computeInfoGain(data, att, ms);
     if (GR_splitting) heuristics[att.index()] = computeGainRatio(data, att,ms);
     counter1++;
   }
double nuevo=heuristics[Utils.maxIndex(heuristics)];
   
   if(nuevo>1 || nuevo<0)
   {
	   
	   System.out.println(Utils.maxIndex(heuristics)+"fallo! Ganacia de informaicon mayor a 1!");
   }
   // selecting attribute according ...
   if (IG_splitting) m_Attribute=data.attribute(Utils.maxIndex(heuristics));
   if (GR_splitting) m_Attribute=data.attribute(Utils.maxIndex(heuristics));
   ////aqui se guarda el atributo
   System.out.println(m_Attribute.toString());
   
  
 
   if (Utils.eq(heuristics[m_Attribute.index()], 0)) // leaf
   {
	   //verfica si el index es 0
     //System.out.println("trobe una fulla ");
	  // View.viewData(data);
		// System.out.println("nohay a
     setNodeAsLeaf(data);
    
     
   }
   else
   {
	   

       splitData = splitData(data, m_Attribute, ms);
       setPrototypesWeight(splitData);
       //View.viewPartition("The split remains as follows ...",splitData);
       m_Successors = new Id3Metric[splitData.length]; // creating the child nodes.
       for (int j = 0; j < splitData.length; j++) {
         m_Successors[j] = new Id3Metric();
  
        m_Successors[j].buildClassifier(splitData[j], ms, m, split_criterion, attributes_method);
        
        
        //View.ViewTree("arbol de decision RAMA"+j+"",m_Successors[j].toString());
         //m_Successors[j].toString(j);
       // System.out.println(" arbol de decision RAMA "+j+m_Successors[j].toString(j));
        
       //}
     }
   }
 };


 

 /**
   Classify an instance using the proximity criterion.
 */
 public double classifyInstance(Instance inst, MetricSpace ms)
 {
   int branch,row,colum;
   double  min;
   
   //secambio el tipo de dato de "d" double a entero
   double d=0;
   double[][] matrix;
   branch =0 ; row =0; colum =0;

   if (m_Attribute == null) return m_ClassValue;
   else
   {
	

	  
     min=Double.MAX_VALUE;
     for (int i=0; i < m_att_prototypes.length; i++)
     {
    

    	
       if (m_Attribute.isNumeric()) {
    	   
    	   //System.out.println("Valor atributo:"+inst.value(m_Attribute));
    	   d=(int) Math.abs(inst.value(m_Attribute) - m_att_prototypes[i]);
    	//System.out.println("valor d:"+d);   
       }
       
       else
    	  if(m_Attribute.isString())
       {
    		  row = (int) inst.value(m_Attribute.index());
              colum = (int) m_att_prototypes[i];
              
             
              d = getMatrixValue(row,colum,m_Attribute.index());
              
     if(row>941 && row<943)
     {
    	 
    	 row=(int) inst.value(m_Attribute.index());
    	 
     }
    // 
       
       System.out.println("valor para string d:"+d+" "+(int) inst.value(m_Attribute.index())+"  "+ (int)m_att_prototypes[i]);   
	   }  else if(m_Attribute.isNominal())
       {
	
    	  // d = matrix[(int) inst.value(att)][(int)m_att_prototypes[k]];
    	   
		   
		   row = (int) inst.value(m_Attribute.index());
           colum = (int) m_att_prototypes[i];
           
          
           //d = mms.getDistanceMatrix(m_Attribute)[colum][row];
	   
       matrix=ms.getDistanceMatrix(m_Attribute);
       d = getMatrixValue(row,colum,m_Attribute.index());
     //System.out.println("valor para nominal d:"+d);   
	   }// else
     //  {
		 
       
       
        if ( d < min)
        {
          min = d;
          branch=i;
        }
     }
     return m_Successors[branch].classifyInstance(inst,ms);
   }
 };


 /**
   Classify an instance using the "gravity" as a criterion.
 */
 public double classifyInstanceDensity(Instance inst, MetricSpace ms)
 {
   int branch,row,colum;
   double max, E,d;

   branch = 0; row = 0 ; colum = 0;
   
   
   if (m_Attribute == null) return m_ClassValue;
   else
   {
     max=Double.MIN_VALUE;
     for (int i=0; i < m_att_prototypes.length; i++)
     {
    	 
    	 if(m_Attribute.isString()){
    		 
  //  		 System.out.println("cadena");
    	 }
       if (m_Attribute.isNumeric()) d = Math.abs(inst.value(m_Attribute) - m_att_prototypes[i]);
       else
       {
        
         row=(int) inst.value(m_Attribute.index());
         colum=(int) m_att_prototypes[i];
         d = getMatrixValue(row,colum,m_Attribute.index());
       }
       if (m_Attribute.isString())
	   {
    	
           
          
            d = getMatrixValue((int) inst.value(m_Attribute.index()),(int) m_att_prototypes[i],m_Attribute.index());
	   }
       E=weight_prototypes[i]/(d*d);
       if (E > max)
       {
         max = E;
         branch=i;
       }
     }
     return m_Successors[branch].classifyInstance(inst,ms);
   }
 };


 /**
   Compute IG taking the metric space into account.
 */
 private double computeInfoGain(Instances data, Attribute att, MetricSpace ms) throws Exception
 {
   Instances[] splitData;

   splitData = splitData(data, att, ms);
   return InformationGain(data,splitData);
 };


 /**
   Compute GR taking the metric space into account.
 */
 private double computeGainRatio(Instances data, Attribute att, MetricSpace ms) throws Exception
 {
   Instances[] splitData;
   Distribution distribution;
   GainRatioSplitCrit c45gainRatio;
   double IG,GR,GR1;
   if(att.isString() && att.index()==0){
	   
	  // System.out.println("cadena"+att.name());
	   
   }
   //System.out.println("Compute Gain ratio"+data.numInstances());
	  
   
   splitData = splitData(data, att, ms);
   
   if(att.isString()){
	   
	   System.out.println(" numero de instancias"+data.numInstances());
   }
   
   distribution = new Distribution(splitData.length,data.numClasses());
   for (int i=0;i<splitData.length;i++)
   {
	   //System.out.println("Split data #"+i);
     Enumeration instances = splitData[i].enumerateInstances();
     while (instances.hasMoreElements())
     {
    	 
       Instance inst = (Instance) instances.nextElement();
      // System.out.println(inst);
       //data.checkInstance(arg0)
    boolean  is_correct=data.checkInstance(inst);
    if(!is_correct){
    	System.out.println("instancia sin strings");
    	
    	
    }
       distribution.add(i,inst);
     }
   }
   
if(att.isString()){
	   
	   System.out.println(" numero de instancias"+data.numInstances());
   }
   
   IG=InformationGain(data,splitData);
   c45gainRatio = new GainRatioSplitCrit();
   GR1=splitCritValuenuevo(distribution,data.numInstances(),IG);
   GR=c45gainRatio.splitCritValue(distribution,data.numInstances(),IG);
   
  // System.out.println("El GR que calcule jo " + IG / SplitInformation(data,splitData));
   //System.out.println("El GR que calcule  WEKA " + GR);
  // System.out.println("El GR que calcule  WEKA GR1 " + GR1);
  
   return GR;
 };

 
 public final double splitCritValuenuevo(Distribution bags, double totalnoInst,
	     double numerator){

	double denumerator;
	double noUnknown;
	double unknownRate;
	int i;
	GainRatioSplitCrit c45gainRatio;
	c45gainRatio = new GainRatioSplitCrit();
	// Compute split info.
	denumerator = splitEntT(bags,totalnoInst);
	
	// Test if split is trivial.
	if (Utils.eq(denumerator,0))
	return 0;  
	denumerator = denumerator/totalnoInst;
	
	return numerator/denumerator;
}
 
 
 private final double splitEntT(Distribution bags,double totalnoInst){
	    
	    double returnValue = 0;
	    double noUnknown;
	    int i;
	    GainRatioSplitCrit c45gainRatio;
	    c45gainRatio = new GainRatioSplitCrit();
	    
	    noUnknown = totalnoInst-bags.total();
	    if (Utils.gr(bags.total(),0)){
	      for (i=0;i<bags.numBags();i++)
		returnValue = returnValue-c45gainRatio.logFunc(bags.perBag(i));
	      returnValue = returnValue-c45gainRatio.logFunc(noUnknown);
	      returnValue = returnValue+c45gainRatio.logFunc(totalnoInst);
	    }
	    return returnValue;
	  }
 
/**
  Compute the IG for a given split.
*/
private double InformationGain(Instances data,Instances [] split) throws Exception
{
  double infoGain;

  infoGain = computeEntropy(data);
  
  for (int j = 0; j < split.length; j++){
    if (split[j].numInstances() > 0)
      infoGain -= ((double) split[j].numInstances() / (double) data.numInstances())
                  * computeEntropy(split[j]);
  }
 
  if(infoGain<0.0){
 //System.out.println("information gain"+infoGain);
  }else if(infoGain>1.0) {
	  //System.out.println("information gain "+infoGain);
	  
  }
 // System.out.println("information gain "+infoGain);
  return infoGain;
};


/**
   Compute the SI for a given split.
*/
private double SplitInformation(Instances data,Instances [] split)
{
  double splitInfo,quotient;

  splitInfo=0.0;
  for (int i=0; i < split.length; i++)
  {
    if (split[i].numInstances() > 0)
    {
      quotient = (double) split[i].numInstances() / (double) data.numInstances();
      splitInfo -= quotient*Utils.log2(quotient);
    }
  }
  return splitInfo;
};


 /**
  Computes the entropy of a dataset.
 */
 private double computeEntropy(Instances data) throws Exception
 {
   double [] classCounts;
   Enumeration instEnum;

   classCounts = new double[data.numClasses()];
   instEnum = data.enumerateInstances();
   while (instEnum.hasMoreElements())
   {
     Instance inst = (Instance) instEnum.nextElement();
     classCounts[(int) inst.classValue()]++;
   }
   double entropy = 0;
   //System.out.println(data.numClasses());
   for (int j = 0; j < data.numClasses(); j++)
   {
     if (classCounts[j] > 0) entropy -= classCounts[j] * Utils.log2(classCounts[j]);
   }
 // entropy= Math.abs(entropy);
   
   entropy /= (double) data.numInstances();
   return entropy+ Utils.log2(data.numInstances());
 };


 /**
   Split a data set taking the properly distance matrix into account.
 */
 private Instances[] splitData(Instances data, Attribute  att, MetricSpace ms) throws Exception
 {
   double matrix[][]; // load the space matrix associated to the attribute
   double d[],d1[]; // array of distances
   double [] prototypes; // array of the prototypes values
   Instances [] splitData;
   Enumeration instEnum;
   double proto;

   //System.out.println("Calling getPrototypes ...");
   prototypes=ms.getPrototypes(data,att,m,attributes_method);//prototypes

   m_att_prototypes = new double[prototypes.length]; // class ID3 Metric attribute
   //ratio_prototypes = new double[prototypes.length];
   for (int i=0;i<prototypes.length;i++)
     {
       m_att_prototypes[i]=prototypes[i];
       //System.out.println("ee "+m_att_prototypes[i]);
     };
   splitData = new Instances[m_att_prototypes.length];

   //empty set of instances
   for (int j = 0; j < m_att_prototypes.length; j++)
     splitData[j] = new Instances(data, data.numInstances());

   instEnum = data.enumerateInstances();
    int counter2=0;
   while (instEnum.hasMoreElements())
   {


     Instance inst = (Instance) instEnum.nextElement();

     // distance from inst (instance) to every prototype
     d = new double[m_att_prototypes.length];
     
     
  //   d1 = new double[m_att_prototypes.length];
        //System.out.println("Contador  splitdata "+ counter2);


     for (int k = 0; k < m_att_prototypes.length; k++)
     {


    System.out.println(att.name()+att.type());
         //proto = prototypes[k];
       matrix = ms.getDistanceMatrix(att.index());
       if (att.isNumeric()){ d[k]=Math.abs(inst.value(att) - m_att_prototypes[k]); 
       // numerical attribute
       }
       else{
    	   // int row = (int) inst.value(m_Attribute.index());
          // int colum = (int) m_att_prototypes[k];
           
          
           //d = mms.getDistanceMatrix(m_Attribute)[colum][row];
    	   
    	   d[k] = getMatrixValue((int)inst.value(att.index()),(int)m_att_prototypes[k],att.index());
       }

   
      
    //   d1[k]=ms.getDistanceMatrix(att.index())[((int) inst.value(att))][((int) m_att_prototypes[k])];
      // System.out.println(d[k]+" "+d1[k]);
     }
    //para normalizar
     //if(d.length>1)
    	// {
    	 //Utils.normalize(d);
    	 
    	 
    	// }
     
 //  for(int i=0;i<d.length;i++){
     
   //         d[i]=d[i]/ Math.pow(10,1);
   //}
    
     //System.out.println("before splitting");
     splitData[Utils.minIndex(d)].add(inst); // drop an instance in one given branch
     //System.out.println("after splitting");
     //if (ratio_prototypes[Utils.minIndex(d)]<d[Utils.minIndex(d)]) ratio_prototypes[Utils.minIndex(d)]=d[Utils.minIndex(d)];
    counter2++;
   };
   return splitData;
 };


 /**
   Set the weight of each prototype.
 */
 private void setPrototypesWeight(Instances [] splitData)
 {
   weight_prototypes=new int[splitData.length];
   for (int i=0; i< splitData.length;i++) weight_prototypes[i]=splitData[i].numInstances();
 };


 /**
   Computes class distribution for instance using decision tree.
   @param instance the instance for which distribution is to be computed
   @return the class distribution for the given instance
 */
 public double[] distributionForInstance(Instance inst)
 {
   double min,d;
   int row,colum,branch;
   branch=0;

   if (m_Attribute == null) return m_Distribution;
   else
     {
       min = Double.MAX_VALUE;
       for (int i = 0; i < m_att_prototypes.length; i++)
       {
         if (m_Attribute.isNumeric()) d = Math.abs(inst.value(m_Attribute) - m_att_prototypes[i]);
         else
         {
           row = (int) inst.value(m_Attribute.index());
           colum = (int) m_att_prototypes[i];
           
          
           d  = getMatrixValue(row,colum,m_Attribute.index());
           
          
         }
         if (d < min) {
           min = d;
           branch = i;
         }
       }
     }
     return m_Successors[branch].distributionForInstance(inst);
 };


 /**
   Set a node (not necessarely pure) as a leaf
 */
 private void setNodeAsLeaf(Instances data)
 {
    m_Attribute = null;
    m_Distribution = new double[data.numClasses()];
    Enumeration instEnum = data.enumerateInstances();

    while (instEnum.hasMoreElements())
    {
      Instance inst = (Instance) instEnum.nextElement();
     // System.out.println((int) inst.classValue());
      m_Distribution[(int) inst.classValue()]++;
    }
    Utils.normalize(m_Distribution);
    m_ClassValue = Utils.maxIndex(m_Distribution);
    m_ClassAttribute = data.classAttribute();
 };


 /**
   Prints the decision tree using the private toString method from below.
   @return a textual description of the classifier
  */
 public String toString()
 {

   if ((m_Distribution == null) && (m_Successors == null)) return "Id3: No model built yet.";
   return "Id3\n\n" + toString(0);
 };


/**
  Outputs a tree at a certain level.
  @param level the level at which the tree is to be printed
 */
 private String toString(int level)
 {

   StringBuffer text = new StringBuffer();


   if (m_Attribute == null) {
     if (Instance.isMissingValue(m_ClassValue)) {
       text.append(": null");
     } else {
       text.append(": "+m_ClassAttribute.value((int) m_ClassValue));
     }
   } else {
     for (int j = 0; j < m_Successors.length; j++) {
       text.append("\n");
       for (int i = 0; i < level; i++) {
         text.append("|  ");
       }
if(m_Attribute.isString()){
       text.append(m_Attribute.name() + " = " + m_Attribute.value(((int) m_att_prototypes[j])));
}else
{
    text.append(m_Attribute.name() + " = " + ((int) m_att_prototypes[j]));
}
       text.append(m_Successors[j].toString(level + 1));

     }
   }
   return text.toString();
 }

 
 public int graphType()
 {
     return Drawable.TREE;
 }

 /**
  * Returns a string that describes the ID3 tree.
  * The output is in dotty format.
  *
  * @return the graph described by a string
  * @throws Exception if the graph can't be computed
  */
 public String graph() throws Exception
 {
     StringBuffer text = new StringBuffer();

     text.append( "digraph ID3Tree {\n" );
     text.append( graph( 0 ) );

     return text.toString() +"}\n";
 }

 /**
  * Returns a string that describes the ID3 tree.
  * The output is in dotty format.
  *
  * @param level the level at which the tree is to be printed
  * @return the graph described by a string
  * @throws Exception if the graph can't be computed
  */
  public String graph(int level) throws Exception
 {
     StringBuffer text = new StringBuffer();
     if ( m_Attribute == null ) {
         if ( Instance.isMissingValue( m_ClassValue ) ) {
             text.append( "N" + m_id + " [label=\"null\" " +
                          "shape=box style=filled " );
         } else {
             text.append( "N" + m_id + " [label=\"" +
                          m_ClassAttribute.value( (int) m_ClassValue ) + "\" " +
                          "shape=box style=filled " );
         }
         text.append( "]\n" );
     } else {
         text.append( "N" + m_id + " [label=\"" +
                      m_Attribute.name() + "\" " );
         text.append( "]\n" );
         for ( int j = 0; j < m_Attribute.numValues(); j++ ) {
             text.append( "N" + m_id + "->" + "N" + m_Successors[j].m_id +
                          " [label=\"= " + m_Attribute.value( j ) +
                          "\"]\n" );
             text.append( m_Successors[j].graph( level + 1 ) );
         }
 }
     return text.toString();
 }

  public double getMatrixValue(int row, int col,int index)
  {
	  
      
      return mms.getDistanceMatrix(index)[row][col];
	  
  }
    
  private void jbInit() throws Exception {
  };

};// end-of-class



