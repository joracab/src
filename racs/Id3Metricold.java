/*
  Id3 metric : Id3 based on metric split condtions.
  This id3 woks on nominal, numerical, and structured attributes.
  Th information gain is used as splitting criterion.
*/
package racs;

import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.classifiers.Evaluation.*;
import weka.classifiers.trees.j48.*;
import weka.core.*;

import java.io.*;
import java.util.*;


public class Id3Metricold extends Classifier
{
  public Id3Metricold() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private int m;                      // maximum number of branches
  private int [] attributes_method;   // median or center or any another thecnique for computing the prototype
                                       /*
                                         0 Median
                                         1 Center
                                         2 ...
                                       */
  private Id3Metricold[] m_Successors;   // The node's successors. (WEKA attribute)
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

 /*
   Constructor: set of instances + metric space + maximum number of children
 */
public void buildClassifierKNN(Instances data,MetricSpace ms,int max_branches,int split_criterion)
{}
 public void buildClassifier(Instances data, MetricSpace ms, int max_branches, int split_criterion, int [] attr_method) throws Exception
 {
   Enumeration enumAtt, enume;

   m=max_branches;
   attributes_method=attr_method;
   mms=ms;
   IG_splitting = false; 
   GR_splitting = false;


   if (split_criterion==0) IG_splitting = true;
   else if(split_criterion==1) GR_splitting=true;

   // checking nominal CLASS value and missing values conditions
   if (!data.classAttribute().isNominal())
      throw new UnsupportedClassTypeException("Id3: nominal class, please.");

  for (int i=0;i<data.numAttributes()-1;i++) data.deleteWithMissing(i);
       //System.out.println("hheeeee");
   enumAtt = data.enumerateAttributes();
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
    //System.out.println("hheeeee");
   makeTree(data,ms,split_criterion); // inferring the decision tree (make a tree using the metric space <ms> and the splitting criterion) !!
 };

 /*
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

   int counter1=0;
   while (attEnum.hasMoreElements())
   {

//     System.out.println("instancias "+counter1);
     Attribute att = (Attribute) attEnum.nextElement(); // checking the attribute
    if ((counter1)==13){
      System.out.println("Error");
    };

     if (IG_splitting) heuristics[att.index()] = computeInfoGain(data, att, ms);
     if (GR_splitting) heuristics[att.index()] = computeGainRatio(data, att,ms);
     counter1++;
   }

   // selecting attribute according ...
   if (IG_splitting) m_Attribute=data.attribute(Utils.maxIndex(heuristics));
   if (GR_splitting) m_Attribute=data.attribute(Utils.maxIndex(heuristics));

   if (Utils.eq(heuristics[m_Attribute.index()], 0)) // leaf
   {
     //System.out.println("trobe una fulla ");
     setNodeAsLeaf(data);
     /*m_Attribute = null;
     m_Distribution = new double[data.numClasses()];
     instEnum = data.enumerateInstances();

     while (instEnum.hasMoreElements())
     {
       Instance inst = (Instance) instEnum.nextElement();
       m_Distribution[(int) inst.classValue()]++;
     }
     Utils.normalize(m_Distribution);
     m_ClassValue = Utils.maxIndex(m_Distribution);
     m_ClassAttribute = data.classAttribute();*/
   }
   else
   {
     //System.out.println("NO NO FULLA ");
     /***************************************/
     //if (data.numInstances()==2)
     //{
       //setNodeAsLeaf(data);
       //System.out.println(" "+racsUtils.getDataClassValues(data));
       //classes=racsUtils.getDataClassValues(data);
       //more_frequent_class=-1;
       //for (int i =0; i<classes.size();i++)
       //{
         //extracted_class=((Double)classes.get(i)).doubleValue();
         //System.out.println("Classes que trobe..."+extracted_class);
         //frequency=racsUtils.original_class_distribution[(int) extracted_class];
         //System.out.println("Frequency en la distribucio original..."+frequency);
         //if (frequency>more_frequent_class) m_ClassValue=extracted_class;
         //else if (frequency==more_frequent_class && extracted_class < m_ClassValue) m_ClassValue=extracted_class;
       //}
       //System.out.println("Escollim la classe "+m_ClassValue);
      //}
     //else
     //{
       splitData = splitData(data, m_Attribute, ms);
       setPrototypesWeight(splitData);
       //View.viewPartition("The split remains as follows ...",splitData);
       m_Successors = new Id3Metricold[splitData.length]; // creating the child nodes.
       for (int j = 0; j < splitData.length; j++) {
         m_Successors[j] = new Id3Metricold();
         
       
  
        m_Successors[j].buildClassifier(splitData[j], ms, m,
                                         split_criterion, attributes_method);
        
        
        //View.ViewTree("arbol de decision RAMA"+j+"",m_Successors[j].toString());
         //m_Successors[j].toString(j);
       // System.out.println(" arbol de decision RAMA "+j+m_Successors[j].toString(j));
        
       //}
     }
   }
 };


 /*
   Classify an instance using the proximity criterion.
 */
 public double classifyInstance(Instance inst, MetricSpace ms)
 {
   int branch,row,colum;
   double d, min;

   branch =0 ; row =0; colum =0;
   if (m_Attribute == null) return m_ClassValue;
   else
   {
     min=Double.MAX_VALUE;
     for (int i=0; i < m_att_prototypes.length; i++)
     {
       if (m_Attribute.isNumeric()) d=Math.abs(inst.value(m_Attribute) - m_att_prototypes[i]);
       else
       {
         row=(int) inst.value(m_Attribute.index());
         colum=(int) m_att_prototypes[i];
         d=ms.getDistanceMatrix(m_Attribute)[row][colum];
        }
        if ( d < min)
        {
          min = d;
          branch=i;
        }
     }
     return m_Successors[branch].classifyInstance(inst,ms);
   }
 };


 /*
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
       if (m_Attribute.isNumeric()) d = Math.abs(inst.value(m_Attribute) - m_att_prototypes[i]);
       else
       {
         row=(int) inst.value(m_Attribute.index());
         colum = (int) m_att_prototypes[i];
         d = ms.getDistanceMatrix(m_Attribute)[row][colum];
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


 /*
   Compute IG taking the metric space into account.
 */
 private double computeInfoGain(Instances data, Attribute att, MetricSpace ms) throws Exception
 {
   Instances[] splitData;

   splitData = splitData(data, att, ms);
   return InformationGain(data,splitData);
 };


 /*
   Compute GR taking the metric space into account.
 */
 private double computeGainRatio(Instances data, Attribute att, MetricSpace ms) throws Exception
 {
   Instances[] splitData;
   Distribution distribution;
   GainRatioSplitCrit c45gainRatio;
   double IG,GR;

   splitData = splitData(data, att, ms);
   distribution = new Distribution(splitData.length,data.numClasses());
   for (int i=0;i<splitData.length;i++)
   {
     Enumeration instances = splitData[i].enumerateInstances();
     while (instances.hasMoreElements())
     {
       Instance inst = (Instance) instances.nextElement();
       distribution.add(i,inst);
     }
   }
   IG=InformationGain(data,splitData);
   c45gainRatio = new GainRatioSplitCrit();
   GR=c45gainRatio.splitCritValue(distribution,data.numInstances(),IG);
   //System.out.println("El GR que calcule jo " + IG / SplitInformation(data,splitData));
   //System.out.println("El GR que calcule  WEKA " + GR);
   return GR;
 };

/*
  Compute the IG for a given split.
*/
private double InformationGain(Instances data,Instances [] split) throws Exception
{
  double infoGain;

  infoGain = computeEntropy(data);
  for (int j = 0; j < split.length; j++)
    if (split[j].numInstances() > 0)
      infoGain -= ((double) split[j].numInstances() / (double) data.numInstances())
                  * computeEntropy(split[j]);
  return infoGain;
};


/*
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


 /*
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
   for (int j = 0; j < data.numClasses(); j++)
   {
     if (classCounts[j] > 0) entropy -= classCounts[j] * Utils.log2(classCounts[j]);
   }
   entropy /= (double) data.numInstances();
   return entropy+ Utils.log2(data.numInstances());
 };


 /*
   Split a data set taking the properly distance matrix into account.
 */
 private Instances[] splitData(Instances data, Attribute  att, MetricSpace ms) throws Exception
 {
   double matrix[][]; // load the space matrix associated to the attribute
   double d[]; // array of distances
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

        //System.out.println("Contador  splitdata "+ counter2);


     for (int k = 0; k < m_att_prototypes.length; k++)
     {



         //proto = prototypes[k];
       matrix = ms.getDistanceMatrix(att.index());
       if (matrix==null) d[k]=Math.abs(inst.value(att) - m_att_prototypes[k]);  // numerical attribute
       else d[k] = matrix[(int) inst.value(att)][(int)m_att_prototypes[k]];


     }
     //System.out.println("before splitting");
     splitData[Utils.minIndex(d)].add(inst); // drop an instance in one given branch
     //System.out.println("after splitting");
     //if (ratio_prototypes[Utils.minIndex(d)]<d[Utils.minIndex(d)]) ratio_prototypes[Utils.minIndex(d)]=d[Utils.minIndex(d)];
    counter2++;
   };
   return splitData;
 };


 /*
   Set the weight of each prototype.
 */
 private void setPrototypesWeight(Instances [] splitData)
 {
   weight_prototypes=new int[splitData.length];
   for (int i=0; i< splitData.length;i++) weight_prototypes[i]=splitData[i].numInstances();
 };


 /*
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
           d = mms.getDistanceMatrix(m_Attribute)[row][colum];
         }
         if (d < min) {
           min = d;
           branch = i;
         }
       }
     }
     return m_Successors[branch].distributionForInstance(inst);
 };


 /*
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
      m_Distribution[(int) inst.classValue()]++;
    }
    Utils.normalize(m_Distribution);
    m_ClassValue = Utils.maxIndex(m_Distribution);
    m_ClassAttribute = data.classAttribute();
 };


 /*
   Prints the decision tree using the private toString method from below.
   @return a textual description of the classifier
  */
 public String toString()
 {

   if ((m_Distribution == null) && (m_Successors == null)) return "Id3: No model built yet.";
   return "Id3\n\n" + toString(0);
 };


/*
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

       text.append(m_Attribute.name() + " = " +   m_att_prototypes[j]);
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
/* public String graph() throws Exception
 {
     StringBuffer text = new StringBuffer();

     text.append( "digraph ID3Tree {\n" );
     text.append( graph( 0 ) );

     return text.toString() +"}\n";
 }
*/
 /**
  * Returns a string that describes the ID3 tree.
  * The output is in dotty format.
  *
  * @param level the level at which the tree is to be printed
  * @return the graph described by a string
  * @throws Exception if the graph can't be computed
  *//*
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
*/
  private void jbInit() throws Exception {
  };

};// end-of-class



