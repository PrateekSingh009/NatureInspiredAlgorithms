package com.algo.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PSO {
	
	Function func;
	int dim;
	int sample_size;
	double w;
	double c1;
	double c2;
	List<List<Double>> position;
	List<List<Double>> velocity;
	List<Double> fitness;
	List<Double> perFitness;
	double gloFitness;
	char typeFunction;
	double[] bounds;
	List<List<Double>> bestPerPosition;
	List<Double> bestGloPosition;
	
	
	public PSO(Function func,
			double[] bounds,
			int dim,
			int sample_size,
			double w,
			double c1,
			double c2,
			char typeFunction)
	{
		this.func = func;
		this.bounds = bounds;
		this.dim = dim;
		this.sample_size = sample_size;
		this.w = w;
		this.c2 = c2;
		this.c1 = c1;
		this.typeFunction = typeFunction;
		position = new ArrayList<List<Double>>();
		bestPerPosition = new ArrayList<List<Double>>();
		velocity = new ArrayList<List<Double>>();
		bestGloPosition = new ArrayList<Double>();
		perFitness = new ArrayList<Double>();
		
		
	}
	

	
	public void initializePopulation(){
	
		List<List<Double>> pop = new ArrayList<List<Double>>();
		List<List<Double>> velo = new ArrayList<List<Double>>();
		
		for(int i=0;i<this.sample_size;i++)
		{
			//generating positions randomly
			List<Double> iPos = new ArrayList<Double>();
			for(int j=0;j<this.dim;j++)
			{
				iPos.add(getRandomDouble(this.bounds[0],this.bounds[1]));
			}
			pop.add(iPos);
			
			//generating velocity randomly
			List<Double> iVelo = new ArrayList<Double>();
			for(int j=0;j<this.dim;j++)
			{
				iVelo.add(getRandomDouble(this.bounds[0],this.bounds[1]));
			}
			velo.add(iVelo);
			
			
		}
		this.position= pop;
		this.velocity= velo;
	}
	
	
	public void calFitnessInitBest()
	{
		this.fitness = new ArrayList<Double>();
		
		for(int i=0;i<this.sample_size;i++) {
			
			double funcVal = func.eval(conversionListToArray(this.position.get(i)), this.typeFunction);
			
			this.fitness.add(funcVal);
	
		}

		//Initially best value of all the position is the initial value
		this.bestPerPosition = new ArrayList<List<Double>>(this.position);
		//Position of the best fitness
		this.perFitness = new ArrayList<Double>(this.fitness);
		this.bestGloPosition = this.position.get(this.fitness.indexOf(Collections.min(this.fitness)));
		this.gloFitness = Collections.min(this.fitness);	
		
	} 
	
	public void generateSolution() {
		
		for(int i=0;i<this.sample_size;i++)
		{
			
			List<Double> ivelo = new ArrayList<Double>();
			
			for(int j=0;j<this.dim;j++) // generating random vectors
			{
				double val = this.w*this.velocity.get(i).get(j) +  
						this.c1*getRandomNumber(0,1)*(this.bestPerPosition.get(i).get(j) - this.position.get(i).get(j)) + 
						this.c2*getRandomNumber(0,1)*(this.bestGloPosition.get(j) - this.position.get(i).get(j));
				
				ivelo.add(val);
			}
			
			this.velocity.set(i, ivelo);
			
			List<Double> inewSol = new ArrayList<Double>();
			
			for(int j=0;j<this.dim;j++) {
				
				double val  = this.position.get(i).get(j) + ivelo.get(j);
				
				// Making sure that the values should be in bounds
				if(val<this.bounds[0])
					val=this.bounds[0];
				if(val>this.bounds[1])
					val=this.bounds[1];
	
				inewSol.add(val);
	
			}

			this.position.set(i, inewSol);
			
			double funcVal = func.eval(conversionListToArray(this.position.get(i)), this.typeFunction);
			
			this.fitness.set(i,funcVal); 
		
			//Checking for personal fitness
			if(this.perFitness.get(i) > this.fitness.get(i)) {
				this.bestPerPosition.set(i, this.position.get(i));
				this.perFitness.set(i, this.fitness.get(i));
			}
			
			if(this.fitness.get(i) < this.gloFitness) {
				this.bestGloPosition = this.position.get(i);
				this.gloFitness = this.fitness.get(i);	
			}	
		}
	}
	
	
	public double[] conversionListToArray(List<Double> val) {
		double arr[] = new double[this.dim];
		for (int x = 0; x < this.dim; x++)
            arr[x] = val.get(x);
		
		return arr;
	}
	
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
