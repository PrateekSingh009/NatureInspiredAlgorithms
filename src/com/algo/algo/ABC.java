package com.algo.algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ABC {
	
	Function func;
	int dim;
	int sample_size;
	int swarm_var ;
	List<List<Double>> population;
	double phi;
	List<Double> fitness;
	char typeFunction;
	double[] bounds;
	int[] trials;
	double[] prob;
	int limit;
	

	public ABC(Function func,
			double[] bounds,
			int dim,
			int swarm_var,
			double phi,
			char typeFunction) {
		
		this.func = func;
		this.dim = dim;
		this.bounds = bounds;
		this.swarm_var = swarm_var;
		this.sample_size = swarm_var/2;
		this.phi=phi;
		this.typeFunction = typeFunction;	
		population = new ArrayList<List<Double>>();
		fitness = new ArrayList<Double>();
		trials = new int[sample_size];
		Arrays.fill(trials,0);
		prob = new double[sample_size];
		limit = sample_size*dim;

	}
	
	/*       Initializing New Population        */
	
	public void initializePopulation(){
		
		List<List<Double>> pop = new ArrayList<List<Double>>();
		
		for(int i=0;i<this.sample_size;i++)
		{
			List<Double> iParent = new ArrayList<Double>();
			for(int j=0;j<this.dim;j++)
			{
				iParent.add(getRandomDouble(this.bounds[0],this.bounds[1]));
			}
			pop.add(iParent);
		}
		this.population = pop;
	}

	/*     Fitness Value calculation     */
	
	public void calculateFitness()
	{
		this.fitness = new ArrayList<Double>();
		
		for(int i=0;i<this.sample_size;i++) {
			
			double funcVal = func.eval(conversionListToArray(this.population.get(i)), this.typeFunction);
			
			this.fitness.add(fitnessValue(funcVal));
	
		}
	}
	
	/*     Employeed Bee Phase     */
	
	public void employeedBeePhase() {
		
		
		
		for(int i=0;i<this.sample_size;i++) {
			
			int p;
			do {
				
				p = getRandomInteger(0,this.sample_size-1);
				
			}while(i==p);
			
			int j = getRandomInteger(0,this.dim-1);

			double j_variable = this.population.get(i).get(j);
			
			double j_variable_new = j_variable + this.phi*(j_variable - this.population.get(p).get(j));
			
			if(j_variable_new<this.bounds[0])
				j_variable_new=this.bounds[0];
			if(j_variable_new>this.bounds[1])
				j_variable_new=this.bounds[1];
			
			List<Double> new_sol = new ArrayList<>(this.population.get(i));
			new_sol.set(j, j_variable_new);
			
			double funcVal = func.eval(conversionListToArray(new_sol), this.typeFunction);
			double new_sol_fitness = fitnessValue(funcVal);
			
			if(new_sol_fitness>this.fitness.get(i)) {
				this.population.set(i,new_sol);
				this.fitness.set(i, new_sol_fitness);//=======
				this.trials[i]=0;
			}
			else {
				this.trials[i]++;
			}
	
		}
		
	}
	
	/*      Calculating the Fitness Probabilities     */
	
	public void probCalculator(){
		
		double fit_sum = 0.0;
		for(double d : this.fitness) {
			fit_sum+=d;
		}
		
		for(int i=0;i<this.sample_size;i++)
		{
			this.prob[i] = 0.9*(this.fitness.get(i)/fit_sum)+0.1;
		}
		
	}
	
	/*     OnLooker Bee Phase     */
	
	public void onLookerBeePhase() {
		
		int m = 0,i = 0;

		while(m<=this.sample_size) {
			
			double r = getRandomNumber(0,1);
			if(r<this.prob[i]) {
				
				
				int p;
				do {
					
					p = getRandomInteger(0,this.sample_size-1);
					
				}while(i==p);
				
				int j = getRandomInteger(0,this.dim-1);

				double j_variable = this.population.get(i).get(j);
				
				double j_variable_new = j_variable + this.phi*(j_variable - this.population.get(p).get(j));
				
				if(j_variable_new<this.bounds[0])
					j_variable_new=this.bounds[0];
				if(j_variable_new>this.bounds[1])
					j_variable_new=this.bounds[1];
				
				List<Double> new_sol = new ArrayList<>(this.population.get(i));
				new_sol.set(j, j_variable_new);
				
				double funcVal = func.eval(conversionListToArray(new_sol), this.typeFunction);
				double new_sol_fitness = fitnessValue(funcVal);
				
				if(new_sol_fitness>this.fitness.get(i)) {
					this.population.set(i,new_sol);
					this.fitness.set(i, new_sol_fitness);
					this.trials[i]=0;
				}
				else {
					this.trials[i]++;
				}
		
				m=m+1;
				
			}
			else continue;
			
			i=i+1;
			if(i>=this.sample_size) i=0;
			
			
		}

	}
	
	/*     Scout Bee Phase     */
	
	public void scoutBeePhase() {
		 
		int max_trail=-1;
		int pos = -1; 
		for(int i=0;i<this.sample_size;i++)
		{
			if(this.trials[i]>this.limit && this.trials[i]>max_trail);
			{
				max_trail = this.trials[i];
				pos=i;
	
			}
		}
		
		
		List<Double> newParent = new ArrayList<Double>();
		for(int j=0;j<this.dim;j++)
		{
			newParent.add(getRandomDouble(this.bounds[0],this.bounds[1]));
		}
		this.population.set(pos, newParent);
		double funcVal = func.eval(conversionListToArray(newParent), this.typeFunction);
		double new_sol_fitness = fitnessValue(funcVal);
		this.fitness.set(pos, new_sol_fitness);
		this.trials[pos] = 0;
		
		
		
	}
	
	/*     Memorizing the Best Solution so far     */
	
	public List<Double> memorizeBestSol() {
		 
		double max_fit=Collections.max(this.fitness);
		int pos = this.fitness.indexOf(max_fit); 
		
		return this.population.get(pos);
		
	}
	
	/*     Converting and ArrayList to an Array     */
	
	public double[] conversionListToArray(List<Double> val) {
		double arr[] = new double[this.dim];
		for (int x = 0; x < this.dim; x++)
            arr[x] = val.get(x);
		
		return arr;
	}
	
	/*     Fitness Value calculation     */
	
	public double fitnessValue(double val) {
		if(val >= 0)
			return 1/(1+val);
		else
			return (1+Math.abs(val));
		
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

	public static double getRandomDouble(double min,double max) {
		return min + (max - min) * (new Random().nextDouble());
	}
	
	
	

}
