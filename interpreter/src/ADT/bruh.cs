using System;
namespace ParticleSwarmTraining
{
  class ParticleTrainingProgram
  {
    static void Main(string[] args)
    {
      Console.WriteLine("\nBegin neural network training with particle swarm optimization demo\n");
      Console.WriteLine("Data is a 30-item subset of the famous Iris flower set");
      Console.WriteLine("Data is sepal length, width, petal length, width -> iris species");
      Console.WriteLine("Iris setosa = 0 0 1, Iris versicolor = 0 1 0, Iris virginica = 1 0 0 ");
      Console.WriteLine("Predicting species from sepal length & width, petal length & width\n");

      // this is a 30-item subset of the 150-item set
      // data has been randomly assign to train (80%) and test (20%) sets
      // y-value (species) encoding: (0,0,1) = setosa; (0,1,0) = versicolor; (1,0,0) = virginica
      // for simplicity, data has not been normalized as you would in a real scenario

      double[][] trainData = new double[24][];
      trainData[0] = new double[] { 6.3, 2.9, 5.6, 1.8, 1, 0, 0 };
      trainData[1] = new double[] { 6.9, 3.1, 4.9, 1.5, 0, 1, 0 };
      trainData[2] = new double[] { 4.6, 3.4, 1.4, 0.3, 0, 0, 1 };
      trainData[3] = new double[] { 7.2, 3.6, 6.1, 2.5, 1, 0, 0 };
      trainData[4] = new double[] { 4.7, 3.2, 1.3, 0.2, 0, 0, 1 };
      trainData[5] = new double[] { 4.9, 3, 1.4, 0.2, 0, 0, 1 };
      trainData[6] = new double[] { 7.6, 3, 6.6, 2.1, 1, 0, 0 };
      trainData[7] = new double[] { 4.9, 2.4, 3.3, 1, 0, 1, 0 };
      trainData[8] = new double[] { 5.4, 3.9, 1.7, 0.4, 0, 0, 1 };
      trainData[9] = new double[] { 4.9, 3.1, 1.5, 0.1, 0, 0, 1 };
      trainData[10] = new double[] { 5, 3.6, 1.4, 0.2, 0, 0, 1 };
      trainData[11] = new double[] { 6.4, 3.2, 4.5, 1.5, 0, 1, 0 };
      trainData[12] = new double[] { 4.4, 2.9, 1.4, 0.2, 0, 0, 1 };
      trainData[13] = new double[] { 5.8, 2.7, 5.1, 1.9, 1, 0, 0 };
      trainData[14] = new double[] { 6.3, 3.3, 6, 2.5, 1, 0, 0 };
      trainData[15] = new double[] { 5.2, 2.7, 3.9, 1.4, 0, 1, 0 };
      trainData[16] = new double[] { 7, 3.2, 4.7, 1.4, 0, 1, 0 };
      trainData[17] = new double[] { 6.5, 2.8, 4.6, 1.5, 0, 1, 0 };
      trainData[18] = new double[] { 4.9, 2.5, 4.5, 1.7, 1, 0, 0 };
      trainData[19] = new double[] { 5.7, 2.8, 4.5, 1.3, 0, 1, 0 };
      trainData[20] = new double[] { 5, 3.4, 1.5, 0.2, 0, 0, 1 };
      trainData[21] = new double[] { 6.5, 3, 5.8, 2.2, 1, 0, 0 };
      trainData[22] = new double[] { 5.5, 2.3, 4, 1.3, 0, 1, 0 };
      trainData[23] = new double[] { 6.7, 2.5, 5.8, 1.8, 1, 0, 0 };

      double[][] testData = new double[6][];
      testData[0] = new double[] { 4.6, 3.1, 1.5, 0.2, 0, 0, 1 };
      testData[1] = new double[] { 7.1, 3, 5.9, 2.1, 1, 0, 0 };
      testData[2] = new double[] { 5.1, 3.5, 1.4, 0.2, 0, 0, 1 };
      testData[3] = new double[] { 6.3, 3.3, 4.7, 1.6, 0, 1, 0 };
      testData[4] = new double[] { 6.6, 2.9, 4.6, 1.3, 0, 1, 0 };
      testData[5] = new double[] { 7.3, 2.9, 6.3, 1.8, 1, 0, 0 };

      Console.WriteLine("The training data is:");
      ShowMatrix(trainData, trainData.Length, 1, true);

      Console.WriteLine("The test data is:");
      ShowMatrix(testData, testData.Length, 1, true);

      Console.WriteLine("\nCreating a 4-input, 6-hidden, 3-output neural network");
      Console.WriteLine("Using tanh and softmax activations");
      const int numInput = 4;
      const int numHidden = 6;
      const int numOutput = 3;
      NeuralNetwork nn = new NeuralNetwork(numInput, numHidden, numOutput);

      int numParticles = 12;
      int maxEpochs = 700;
      double exitError = 0.060;
      double probDeath = 0.005;

      Console.WriteLine("Setting numParticles = " + numParticles);
      Console.WriteLine("Setting maxEpochs = " + maxEpochs);
      Console.WriteLine("Setting early exit MSE error = " + exitError.ToString("F3"));
      Console.WriteLine("Setting probDeath = " + probDeath.ToString("F3"));
      // other optional PSO parameters (weight decay, death, etc) here

      Console.WriteLine("\nBeginning training using a particle swarm\n");
      double[] bestWeights = nn.Train(trainData, numParticles, maxEpochs, exitError, probDeath);
      Console.WriteLine("Training complete");
      Console.WriteLine("Final neural network weights and bias values:");
      ShowVector(bestWeights, 10, 3, true);

      nn.SetWeights(bestWeights);
      double trainAcc = nn.Accuracy(trainData);
      Console.WriteLine("\nAccuracy on training data = " + trainAcc.ToString("F4"));

      double testAcc = nn.Accuracy(testData);
      Console.WriteLine("\nAccuracy on test data = " + testAcc.ToString("F4"));

      Console.WriteLine("\nEnd neural network training with particle swarm optimization demo\n");
      Console.ReadLine();

    } // Main

    static void ShowVector(double[] vector, int valsPerRow, int decimals, bool newLine)
    {
      for (int i = 0; i < vector.Length; ++i)
      {
        if (i % valsPerRow == 0) Console.WriteLine("");
        Console.Write(vector[i].ToString("F" + decimals).PadLeft(decimals + 4) + " ");
      }
      if (newLine == true) Console.WriteLine("");
    }

    static void ShowMatrix(double[][] matrix, int numRows, int decimals, bool newLine)
    {
      for (int i = 0; i < numRows; ++i)
      {
        Console.Write(i.ToString().PadLeft(3) + ": ");
        for (int j = 0; j < matrix[i].Length; ++j)
        {
          if (matrix[i][j] >= 0.0) Console.Write(" "); else Console.Write("-"); ;
          Console.Write(Math.Abs(matrix[i][j]).ToString("F" + decimals) + " ");
        }
        Console.WriteLine("");
      }
      if (newLine == true) Console.WriteLine("");
    }

  } // class Program

  public class NeuralNetwork
  {
    //private static Random rnd; // for BP to initialize wts, in PSO 
    private int numInput;
    private int numHidden;
    private int numOutput;
    private double[] inputs;
    private double[][] ihWeights; // input-hidden
    private double[] hBiases;
    private double[] hOutputs;
    private double[][] hoWeights; // hidden-output
    private double[] oBiases;
    private double[] outputs;

    public NeuralNetwork(int numInput, int numHidden, int numOutput)
    {
      //rnd = new Random(16); // for particle initialization. 16 just gives nice demo
      this.numInput = numInput;
      this.numHidden = numHidden;
      this.numOutput = numOutput;
      this.inputs = new double[numInput];
      this.ihWeights = MakeMatrix(numInput, numHidden);
      this.hBiases = new double[numHidden];
      this.hOutputs = new double[numHidden];
      this.hoWeights = MakeMatrix(numHidden, numOutput);
      this.oBiases = new double[numOutput];
      this.outputs = new double[numOutput];
    } // ctor

    private static double[][] MakeMatrix(int rows, int cols) // helper for ctor
    {
      double[][] result = new double[rows][];
      for (int r = 0; r < result.Length; ++r)
        result[r] = new double[cols];
      return result;
    }

   

    // ----------------------------------------------------------------------------------------

    public void SetWeights(double[] weights)
    {
      // copy weights and biases in weights[] array to i-h weights, i-h biases, h-o weights, h-o biases
      int numWeights = (numInput * numHidden) + (numHidden * numOutput) + numHidden + numOutput;
      if (weights.Length != numWeights)
        throw new Exception("Bad weights array length: ");

      int k = 0; // points into weights param

      for (int i = 0; i < numInput; ++i)
        for (int j = 0; j < numHidden; ++j)
          ihWeights[i][j] = weights[k++];
      for (int i = 0; i < numHidden; ++i)
        hBiases[i] = weights[k++];
      for (int i = 0; i < numHidden; ++i)
        for (int j = 0; j < numOutput; ++j)
          hoWeights[i][j] = weights[k++];
      for (int i = 0; i < numOutput; ++i)
        oBiases[i] = weights[k++];
    }

    public double[] GetWeights()
    {
      // returns the current set of wweights, presumably after training
      int numWeights = (numInput * numHidden) + (numHidden * numOutput) + numHidden + numOutput;
      double[] result = new double[numWeights];
      int k = 0;
      for (int i = 0; i < ihWeights.Length; ++i)
        for (int j = 0; j < ihWeights[0].Length; ++j)
          result[k++] = ihWeights[i][j];
      for (int i = 0; i < hBiases.Length; ++i)
        result[k++] = hBiases[i];
      for (int i = 0; i < hoWeights.Length; ++i)
        for (int j = 0; j < hoWeights[0].Length; ++j)
          result[k++] = hoWeights[i][j];
      for (int i = 0; i < oBiases.Length; ++i)
        result[k++] = oBiases[i];
      return result;
    }

    // ----------------------------------------------------------------------------------------

    public double[] ComputeOutputs(double[] xValues)
    {
      if (xValues.Length != numInput)
        throw new Exception("Bad xValues array length");

      double[] hSums = new double[numHidden]; // hidden nodes sums scratch array
      double[] oSums = new double[numOutput]; // output nodes sums

      for (int i = 0; i < xValues.Length; ++i) // copy x-values to inputs
        this.inputs[i] = xValues[i];

      for (int j = 0; j < numHidden; ++j)  // compute i-h sum of weights * inputs
        for (int i = 0; i < numInput; ++i)
          hSums[j] += this.inputs[i] * this.ihWeights[i][j]; // note +=

      for (int i = 0; i < numHidden; ++i)  // add biases to input-to-hidden sums
        hSums[i] += this.hBiases[i];

      for (int i = 0; i < numHidden; ++i)   // apply activation
        this.hOutputs[i] = HyperTanFunction(hSums[i]); // hard-coded

      for (int j = 0; j < numOutput; ++j)   // compute h-o sum of weights * hOutputs
        for (int i = 0; i < numHidden; ++i)
          oSums[j] += hOutputs[i] * hoWeights[i][j];

      for (int i = 0; i < numOutput; ++i)  // add biases to input-to-hidden sums
        oSums[i] += oBiases[i];

      double[] softOut = Softmax(oSums); // softmax activation does all outputs at once for efficiency
      Array.Copy(softOut, outputs, softOut.Length);

      double[] retResult = new double[numOutput]; // could define a GetOutputs method instead
      Array.Copy(this.outputs, retResult, retResult.Length);
      return retResult;
    } // ComputeOutputs

    private static double HyperTanFunction(double x)
    {
      if (x < -20.0) return -1.0; // approximation is correct to 30 decimals
      else if (x > 20.0) return 1.0;
      else return Math.Tanh(x);
    }

    private static double[] Softmax(double[] oSums) 
    {
      // does all output nodes at once so scale doesn't have to be re-computed each time
      // determine max output sum
      double max = oSums[0];
      for (int i = 0; i < oSums.Length; ++i)
        if (oSums[i] > max) max = oSums[i];

      // determine scaling factor -- sum of exp(each val - max)
      double scale = 0.0;
      for (int i = 0; i < oSums.Length; ++i)
        scale += Math.Exp(oSums[i] - max);

      double[] result = new double[oSums.Length];
      for (int i = 0; i < oSums.Length; ++i)
        result[i] = Math.Exp(oSums[i] - max) / scale;

      return result; // now scaled so that xi sum to 1.0
    }

    // ----------------------------------------------------------------------------------------

    public double[] Train(double[][] trainData, int numParticles, int maxEpochs, double exitError, double probDeath)
    {
      // PSO version training. best weights stored into NN and returned
      // particle position == NN weights

      Random rnd = new Random(16); // 16 just gives nice demo

      int numWeights = (this.numInput * this.numHidden) + (this.numHidden * this.numOutput) +
        this.numHidden + this.numOutput;

      // use PSO to seek best weights
      int epoch = 0;
      double minX = -10.0; // for each weight. assumes data has been normalized about 0
      double maxX = 10.0;
      double w = 0.729; // inertia weight
      double c1 = 1.49445; // cognitive/local weight
      double c2 = 1.49445; // social/global weight
      double r1, r2; // cognitive and social randomizations
      
      Particle[] swarm = new Particle[numParticles];
      // best solution found by any particle in the swarm. implicit initialization to all 0.0
      double[] bestGlobalPosition = new double[numWeights]; 
      double bestGlobalError = double.MaxValue; // smaller values better

      //double minV = -0.01 * maxX;  // velocities
      //double maxV = 0.01 * maxX;

      // swarm initialization
      // initialize each Particle in the swarm with random positions and velocities
      for (int i = 0; i < swarm.Length; ++i) 
      {
        double[] randomPosition = new double[numWeights];
        for (int j = 0; j < randomPosition.Length; ++j)
        {
          //double lo = minX;
          //double hi = maxX;
          //randomPosition[j] = (hi - lo) * rnd.NextDouble() + lo;
          randomPosition[j] = (maxX - minX) * rnd.NextDouble() + minX;
        }

        // randomPosition is a set of weights; sent to NN
        //double error = MeanCrossEntropy(trainData, randomPosition);
        double error = MeanSquaredError(trainData, randomPosition); 
        double[] randomVelocity = new double[numWeights];

        for (int j = 0; j < randomVelocity.Length; ++j)
        {
          //double lo = -1.0 * Math.Abs(maxX - minX);
          //double hi = Math.Abs(maxX - minX);
          //randomVelocity[j] = (hi - lo) * rnd.NextDouble() + lo;
          double lo = 0.1 * minX;
          double hi = 0.1 * maxX;
          randomVelocity[j] = (hi - lo) * rnd.NextDouble() + lo;

        }
        swarm[i] = new Particle(randomPosition, error, randomVelocity, randomPosition, error); // last two are best-position and best-error

        // does currentbestGlobalError = swarm[i].error;
          swarm[i].position.CopyTo(bestGlobalPosition, 0);
        }
     Particle have global best position/solution?
        if (swarm[i].error < bestGlobalError)
        {
            }
      // initialization

      

      //Console.WriteLine("Entering main PSO weight estimation processing loop");

      // main PSO algorithm

      int[] sequence = new int[numParticles]; // process particles in random order
      for (int i = 0; i < sequence.Length; ++i)
        sequence[i] = i;

      while (epoch < maxEpochs)
      {
        if (bestGlobalError < exitError) break; // early exit (MSE error)

        double[] newVelocity = new double[numWeights]; // step 1
        double[] newPosition = new double[numWeights]; // step 2
        double newError; // step 3

        Shuffle(sequence, rnd); // move particles in random sequence

        for (int pi = 0; pi < swarm.Length; ++pi) // each Particle (index)
        {
          int i = sequence[pi];
          Particle currP = swarm[i]; // for coding convenience

          // 1. compute new velocity
          for (int j = 0; j < currP.velocity.Length; ++j) // each x value of the velocity
          {
            r1 = rnd.NextDouble();
            r2 = rnd.NextDouble();

            // velocity depends on old velocity, best position of parrticle, and 
            // best position of any particle
            newVelocity[j] = (w * currP.velocity[j]) +
              (c1 * r1 * (currP.bestPosition[j] - currP.position[j])) +
              (c2 * r2 * (bestGlobalPosition[j] - currP.position[j])); 
          }

          newVelocity.CopyTo(currP.velocity, 0);

          // 2. use new velocity to compute new position
          for (int j = 0; j < currP.position.Length; ++j)
          {
            newPosition[j] = currP.position[j] + newVelocity[j];  // compute new position
            if (newPosition[j] < minX) // keep in range
              newPosition[j] = minX;
            else if (newPosition[j] > maxX)
              newPosition[j] = maxX;
          }

          newPosition.CopyTo(currP.position, 0);

          // 2b. optional: apply weight decay (large weights tend to overfit) 
          
          // 3. use new position to compute new error
          //newError = MeanCrossEntropy(trainData, newPosition); // makes next check a bit cleaner
          newError = MeanSquaredError(trainData, newPosition);
          currP.error = newError;

          if (newError < currP.bestError) // new particle best?
          {
            newPosition.CopyTo(currP.bestPosition, 0);
            currP.bestError = newError;
          }

          if (newError < bestGlobalError) // new global best?
          {
            newPosition.CopyTo(bestGlobalPosition, 0);
            bestGlobalError = newError;
          }

          // 4. optional: does curr particle die?
          double die = rnd.NextDouble();
          if (die < probDeath)
          {
            // new position, leave velocity, update error
            for (int j = 0; j < currP.position.Length; ++j)
              currP.position[j] = (maxX - minX) * rnd.NextDouble() + minX;
            currP.error = MeanSquaredError(trainData, currP.position);
            currP.position.CopyTo(currP.bestPosition, 0);
            currP.bestError = currP.error;

            if (currP.error < bestGlobalError) // global best by chance?
            {
              bestGlobalError = currP.error;
              currP.position.CopyTo(bestGlobalPosition, 0);
            }
          }
 
        } // each Particle

        ++epoch;

      } // while

      this.SetWeights(bestGlobalPosition);  // best position is a set of weights
      double[] retResult = new double[numWeights];
      Array.Copy(bestGlobalPosition, retResult, retResult.Length);
      return retResult;

    } // Train

    private static void Shuffle(int[] sequence, Random rnd)
    {
      for (int i = 0; i < sequence.Length; ++i)
      {
        int r = rnd.Next(i, sequence.Length);
        int tmp = sequence[r];
        sequence[r] = sequence[i];
        sequence[i] = tmp;
      }
    }

    private double MeanSquaredError(double[][] trainData, double[] weights)
    {
      // assumes that centroids and widths have been set!
      this.SetWeights(weights); // copy the weights to evaluate in

      double[] xValues = new double[numInput]; // inputs
      double[] tValues = new double[numOutput]; // targets
      double sumSquaredError = 0.0;
      for (int i = 0; i < trainData.Length; ++i) // walk through each training data item
      {
        // following assumes data has all x-values first, followed by y-values!
        Array.Copy(trainData[i], xValues, numInput); // extract inputs
        Array.Copy(trainData[i], numInput, tValues, 0, numOutput); // extract targets
        double[] yValues = this.ComputeOutputs(xValues); // compute the outputs using centroids, widths, weights, bias values
        for (int j = 0; j < yValues.Length; ++j)
          sumSquaredError += ((yValues[j] - tValues[j]) * (yValues[j] - tValues[j]));
      }
      return sumSquaredError / trainData.Length;
    }

    //private double MeanCrossEntropy(double[][] trainData, double[] weights) 
    //{
    //  // (average) Cross Entropy for a given particle's position/weights
    //  // how good (cross entropy) are weights? CrossEntropy is error so smaller values are better
    //  this.SetWeights(weights); // load the weights and biases to examine into the NN

    //  double sce = 0.0; // sum of cross entropies of all data items
    //  double[] xValues = new double[numInput]; // inputs
    //  double[] tValues = new double[numOutput]; // targets

    //  // walk thru each training case. looks like (6.9 3.2 5.7 2.3) (0 0 1)
    //  for (int i = 0; i < trainData.Length; ++i)  
    //  {
    //    Array.Copy(trainData[i], xValues, numInput); // extract inputs
    //    Array.Copy(trainData[i], numInput, tValues, 0, numOutput); // extract targets

    //    double[] yValues = this.ComputeOutputs(xValues); // run the inputs through the neural network
    //    // assumes outputs are 'softmaxed' -- all between 0 and 1, and sum to 1

    //    // CE = -Sum( t * log(y) )
    //    // see http://dame.dsf.unina.it/documents/softmax_entropy_VONEURAL-SPE-NA-0004-Rel1.0.pdf 
    //    // for an explanation of why cross tropy sometimes given as CE = -Sum( t * log(t/y) ), as in 
    //    // "On the Pairing of the Softmax Activation and Cross-Entropy Penalty Functions and
    //    // the Derivation of the Softmax Activation Function", Dunne & Campbell.
    //    double currSum = 0.0;
    //    for (int j = 0; j < yValues.Length; ++j)
    //    {
    //      currSum += tValues[j] * Math.Log(yValues[j]); // diff between targets and y
    //    }
    //    sce += currSum; // accumulate
    //  }

    //  return -sce / trainData.Length;
    //} // MeanCrossEntropy



    // ----------------------------------------------------------------------------------------

    public double Accuracy(double[][] testData)
    {
      // percentage correct using winner-takes all
      int numCorrect = 0;
      int numWrong = 0;
      double[] xValues = new double[numInput]; // inputs
      double[] tValues = new double[numOutput]; // targets
      double[] yValues; // computed Y

      for (int i = 0; i < testData.Length; ++i)
      {
        Array.Copy(testData[i], xValues, numInput); // parse test data into x-values and t-values
        Array.Copy(testData[i], numInput, tValues, 0, numOutput);
        yValues = this.ComputeOutputs(xValues);
        int maxIndex = MaxIndex(yValues); // which cell in yValues has largest value?

        if (tValues[maxIndex] == 1.0) // ugly. consider AreEqual(double x, double y)
          ++numCorrect;
        else
          ++numWrong;
      }
      return (numCorrect * 1.0) / (numCorrect + numWrong); // ugly 2 - check for divide by zero
    }

    private static int MaxIndex(double[] vector) // helper for Accuracy()
    {
      // index of largest value
      int bigIndex = 0;
      double biggestVal = vector[0];
      for (int i = 0; i < vector.Length; ++i)
      {
        if (vector[i] > biggestVal)
        {
          biggestVal = vector[i]; bigIndex = i;
        }
      }
      return bigIndex;
    }

  } // NeuralNetwork

  // ==============================================================================================

  public class Particle
  {
    public double[] position; // equivalent to NN weights
    public double error; // measure of fitness
    public double[] velocity;

    public double[] bestPosition; // best position found so far by this Particle
    public double bestError;

    //public double age; // optional used to determine death-birth

    public Particle(double[] position, double error, double[] velocity,
      double[] bestPosition, double bestError)
    {
      this.position = new double[position.Length];
      position.CopyTo(this.position, 0);
      this.error = error;
      this.velocity = new double[velocity.Length];
      velocity.CopyTo(this.velocity, 0);
      this.bestPosition = new double[bestPosition.Length];
      bestPosition.CopyTo(this.bestPosition, 0);
      this.bestError = bestError;

      //this.age = 0;
    }


  } // class Particle

} // ns

      //double[][] allData = new double[30][];
      //allData[0] = new double[] { 5.1, 3.5, 1.4, 0.2, 0, 0, 1 }; // sepal length, sepal width, petal length, petal width -> 
      //allData[1] = new double[] { 4.9, 3.0, 1.4, 0.2, 0, 0, 1 }; // Iris setosa = 0 0 1, Iris versicolor = 0 1 0, Iris virginica = 1 0 0
      //allData[2] = new double[] { 4.7, 3.2, 1.3, 0.2, 0, 0, 1 };
      //allData[3] = new double[] { 4.6, 3.1, 1.5, 0.2, 0, 0, 1 };
      //allData[4] = new double[] { 5.0, 3.6, 1.4, 0.2, 0, 0, 1 };
      //allData[5] = new double[] { 5.4, 3.9, 1.7, 0.4, 0, 0, 1 };
      //allData[6] = new double[] { 4.6, 3.4, 1.4, 0.3, 0, 0, 1 };
      //allData[7] = new double[] { 5.0, 3.4, 1.5, 0.2, 0, 0, 1 };
      //allData[8] = new double[] { 4.4, 2.9, 1.4, 0.2, 0, 0, 1 };
      //allData[9] = new double[] { 4.9, 3.1, 1.5, 0.1, 0, 0, 1 };

      //allData[10] = new double[] { 7.0, 3.2, 4.7, 1.4, 0, 1, 0 };
      //allData[11] = new double[] { 6.4, 3.2, 4.5, 1.5, 0, 1, 0 };
      //allData[12] = new double[] { 6.9, 3.1, 4.9, 1.5, 0, 1, 0 };
      //allData[13] = new double[] { 5.5, 2.3, 4.0, 1.3, 0, 1, 0 };
      //allData[14] = new double[] { 6.5, 2.8, 4.6, 1.5, 0, 1, 0 };
      //allData[15] = new double[] { 5.7, 2.8, 4.5, 1.3, 0, 1, 0 };
      //allData[16] = new double[] { 6.3, 3.3, 4.7, 1.6, 0, 1, 0 };
      //allData[17] = new double[] { 4.9, 2.4, 3.3, 1.0, 0, 1, 0 };
      //allData[18] = new double[] { 6.6, 2.9, 4.6, 1.3, 0, 1, 0 };
      //allData[19] = new double[] { 5.2, 2.7, 3.9, 1.4, 0, 1, 0 };

      //allData[20] = new double[] { 6.3, 3.3, 6.0, 2.5, 1, 0, 0 };
      //allData[21] = new double[] { 5.8, 2.7, 5.1, 1.9, 1, 0, 0 };
      //allData[22] = new double[] { 7.1, 3.0, 5.9, 2.1, 1, 0, 0 };
      //allData[23] = new double[] { 6.3, 2.9, 5.6, 1.8, 1, 0, 0 };
      //allData[24] = new double[] { 6.5, 3.0, 5.8, 2.2, 1, 0, 0 };
      //allData[25] = new double[] { 7.6, 3.0, 6.6, 2.1, 1, 0, 0 };
      //allData[26] = new double[] { 4.9, 2.5, 4.5, 1.7, 1, 0, 0 };
      //allData[27] = new double[] { 7.3, 2.9, 6.3, 1.8, 1, 0, 0 };
      //allData[28] = new double[] { 6.7, 2.5, 5.8, 1.8, 1, 0, 0 };
      //allData[29] = new double[] { 7.2, 3.6, 6.1, 2.5, 1, 0, 0 };

    //static void MakeTrainTest(double[][] allData, out double[][] trainData, out double[][] testData)
    //{
    //  // split allData into 80% trainData and 20% testData
    //  Random rnd = new Random(2);
    //  int totRows = allData.Length;
    //  int numCols = allData[0].Length;

    //  int trainRows = (int)(totRows * 0.80); // hard-coded 80-20 split
    //  int testRows = totRows - trainRows;

    //  trainData = new double[trainRows][];
    //  testData = new double[testRows][];

    //  int[] sequence = new int[totRows]; // create a random sequence of indexes
    //  for (int i = 0; i < sequence.Length; ++i)
    //    sequence[i] = i;

    //  for (int i = 0; i < sequence.Length; ++i)
    //  {
    //    int r = rnd.Next(i, sequence.Length);
    //    int tmp = sequence[r];
    //    sequence[r] = sequence[i];
    //    sequence[i] = tmp;
    //  }

    //  int si = 0; // index into sequence[]
    //  int j = 0; // index into trainData or testData

    //  for (; si < trainRows; ++si) // first rows to train data
    //  {
    //    trainData[j] = new double[numCols];
    //    int idx = sequence[si];
    //    Array.Copy(allData[idx], trainData[j], numCols);
    //    ++j;
    //  }

    //  j = 0; // reset to start of test data
    //  for (; si < totRows; ++si) // remainder to test data
    //  {
    //    testData[j] = new double[numCols];
    //    int idx = sequence[si];
    //    Array.Copy(allData[idx], testData[j], numCols);
    //    ++j;
    //  }

    //  // train data
    //  for (int i = 0; i < trainData.Length; ++i)
    //  {
    //    Console.WriteLine("trainData[" + i + "] = new double[] { " + trainData[i][0] + ", " + trainData[i][1] + ", " + trainData[i][2] + ", " + trainData[i][3] + ", " + trainData[i][4] + ", " + trainData[i][5] + ", " + trainData[i][6] + " };");
    //  }

    //  // test data
    //  for (int i = 0; i < testData.Length; ++i)
    //  {
    //    Console.WriteLine("testData[" + i + "] = new double[] { " + testData[i][0] + ", " + testData[i][1] + ", " + testData[i][2] + ", " + testData[i][3] + ", " + testData[i][4] + ", " + testData[i][5] + ", " + testData[i][6] + " };");
    //  }
    //} // MakeTrainTest

