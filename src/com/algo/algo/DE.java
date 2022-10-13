package com.algo.algo;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class DE {

		
	Function func;
	int dim;
	int sample_size;
	List<List<Double>> population;
	double prob_recomb;
	List<Double> fitness;
	double beta;
	char typeFunction;
	double[] bounds;
	

	public DE(Function func,
			double[] bounds,
			int dim,
			int sample_size,
			double prob_recomb,
			double beta,
			char typeFunction) {
		
		this.func = func;
		this.dim = dim;
		this.bounds = bounds;
		this.sample_size = sample_size;
		this.prob_recomb = prob_recomb;
		this.beta = beta;
		this.typeFunction = typeFunction;	
		population = new ArrayList<List<Double>>();
		fitness = new ArrayList<Double>();
		
		
		
	}
	
	/*       Initializing New Population        */
	
	public void initializePopulation(){
		
		List<List<Double>> pop = new ArrayList<List<Double>>();
		
		for(int i=0;i<this.sample_size;i++)
		{
			List<Double> iParent = new ArrayList<Double>();
			for(int j=0;j<this.dim;j++)
			{
				iParent.add(getRandomNumber(this.bounds[0],this.bounds[1]));
			}
			pop.add(iParent);
		}
		this.population = pop;

		
	}
	
	/*       Fitness Calculation of the initialized population       */
	
	public int calculateFitness() {
		
		double maxfit=0.0;
		int maxfitIdx = 0;
		
		this.fitness = new ArrayList<Double>();
		
		for(int i=0;i<sample_size;i++)
		{
			
			double fit = fitnessValue(this.func.eval(conversionListToArray(this.population.get(i)), this.typeFunction)); // function value of ith sample
			
			if(fit>maxfit)
			{
				maxfit=fit;
				maxfitIdx = i;
			}
			this.fitness.add(fit);
			
		}
		return maxfitIdx;
		
	}
	
	/*     Generating new population and updating the population */
	
	public void reproductionPopulationUpdate(int maxFitIdx) {
		
		List<List<Double>> new_population = new ArrayList<List<Double>>();
		
		for(int j=0;j<this.sample_size;j++) {
			
			List<Double> trialVector = mutation(j,maxFitIdx);  // Mutation
			

			
			List<Double> child = crossover(trialVector,j); // Crossover


			double child_fitness = fitnessValue(this.func.eval(conversionListToArray(child), this.typeFunction));
			
			
			double parent_fitness = this.fitness.get(j);
			
			if(child_fitness > parent_fitness) {
				new_population.add(child);
			}
			else {
				new_population.add(this.population.get(j));
			}

			
		}

		this.population = new_population;
		
		
		
	}
	
    /*      Mutation      */

	public List<Double> mutation(int parent_idx, int target_idx){
		
		List<Double> targetVector = this.population.get(target_idx);  // Target Vector
		
		int rndIdx1=getRandomInteger(0,this.sample_size-1) , rndIdx2=getRandomInteger(0,this.sample_size-1);
		
		while(rndIdx1 == parent_idx || rndIdx1 == target_idx)
			rndIdx1 = getRandomInteger(0,this.sample_size-1); // 
		while(rndIdx2 == parent_idx || rndIdx2 == target_idx || rndIdx2 == rndIdx1)
			rndIdx2 = getRandomInteger(0,this.sample_size-1); // 
		 
		List<Double> randomVector1 = this.population.get(rndIdx1);

		List<Double> randomVector2 = this.population.get(rndIdx2);
		
		List<Double> trialVector = new ArrayList<Double>();
		
		for(int i=0;i<this.dim;i++) {
			
			double valGenerated = targetVector.get(i) + this.beta*(randomVector1.get(i)-randomVector2.get(i));
			
			if(valGenerated < this.bounds[0])
				valGenerated = this.bounds[0];
			
			if(valGenerated > this.bounds[1])
				valGenerated = this.bounds[1];
			
			
			trialVector.add(valGenerated);
		}
		
		
		
		return trialVector;
		
	}
	
	/*       Binomial Crossover     */
	
	public List<Double> crossover(List<Double> trialVector , int parent_idx ) {
		
		List<Double> children = this.population.get(parent_idx);
		int delta = getRandomInteger(0,this.dim-1);
		
		
		for(int i=0;i<this.dim;i++)
		{
			if(getRandomNumber(0,1) < this.prob_recomb || i==delta)
			children.set(i,trialVector.get(i));
			
			
		}
		
		return children;
		
	}

	/*       Getting the Result of the Algorithm       */
	
	public List<Double> result() {
		
		List<Double> res = this.population.get(0);
		double fittest = 0.0;
		
		for(int i=0;i<sample_size;i++)
		{
			
			double fit = fitnessValue(this.func.eval(conversionListToArray(this.population.get(i)), this.typeFunction)); // function value of ith sample
			
			if(fit>fittest)
			{
				fittest=fit;
				res = this.population.get(i);
			}
			
			
		}
		return res;
	
		
	}
	
	/*     Fitness Value calculation     */
	
	public double fitnessValue(double val) {
		if(val == 0)
			return 100000.0;
		else
			return (1/val);
		
	}
	
	/*     Converting and ArrayList to an Array     */
	
	public double[] conversionListToArray(List<Double> val) {
		double arr[] = new double[this.dim];
		for (int x = 0; x < this.dim; x++)
            arr[x] = val.get(x);
		
		return arr;
	}
	
	/*      Functions for generating Random numbers     */
	
	
	public double getRandomNumber(double min, double max) {
	    return ((Math.random() * (max - min)) + min);
	}
	
	public static int getRandomInteger(int Min, int Max)
    {
        return ThreadLocalRandom
            .current()
            .nextInt(Min, Max + 1);
    }
	

}
