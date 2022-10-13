package com.algo.algo;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

public class GA {
	
	
	Function func;
	int dim;
	int sample_size;
	double[] bounds;
	int soln_len;
	List<String> population;
	double prob_cross;
	double prob_muta;
	int child_len;
	List<Double> fitness; 
	char typeFunction;
	List<Double> prob_selection;
	
	
	public GA(Function func,
			int dim,
			int sample_size,
			double[] bound,
			double prob_cross,
			double prob_muta,
			int child_len,
			char typeFunction) {
		
		this.func=func;
		this.dim = dim;
		this.sample_size = sample_size;
		this.typeFunction = typeFunction;
		this.bounds = bound;
		this.soln_len = 0;
		this.prob_cross = prob_cross;
		this.prob_muta = prob_muta;
		this.child_len = child_len;
		List<String> population = new ArrayList<String>();
		List<Double> fitness = new ArrayList<Double>();
		List<Double> prob_selection= new ArrayList<Double>();
		
		
	}
	
	/*       Initializing New Population        */
	
	public void initializePopulation(){
		
		this.population = new ArrayList<String>();
		
		for(int i=0;i<sample_size;i++)  
		{
			
			String child = "";
			for(int j=0;j<this.dim;j++) {
				
				int x =  getRandomInteger(0,(int)Math.pow(2, this.child_len)-1);
				String x_binary = Integer.toBinaryString(x);
				int prev_len = x_binary.length();
				for(int app=1;app<=this.child_len- prev_len;app++)
  					x_binary = '0'+x_binary;
				
				child+=x_binary;
			}
			this.population.add(child);
	
		}
		this.soln_len = this.child_len*this.dim;
	}
	
	/*       Fitness Calculation of the initialized population       */
	
	public void calculateFitness() {
		
		this.fitness = new ArrayList<Double>();
		int var_len = this.soln_len/this.dim;
		
		
		for(String pop : this.population) {
			double values[] = new double[this.dim];
			int cnt=0;
			for(int i=0;i<this.soln_len;i+=var_len) {
				String var = pop.substring(i,i+var_len);
				int var_int = Integer.parseInt(var,2);
				double var_double = this.bounds[0] + ( ( ( this.bounds[1] - this.bounds[0] ) / ( ((int)Math.pow(2,var_len))-1) ) * var_int);
				values[cnt++]=var_double;
			}
			double func_value = this.func.eval(values,this.typeFunction); 
			this.fitness.add(1/func_value);
		}	
	}
	
	/*       Calculating the Probability of Selection of each solution on the basis of fitness       */
	
	public void calculateProbRoulette() {
		
		this.prob_selection = new ArrayList<Double>();
		double total_fitness = 0;
	    for (double i: this.fitness) 
	    	total_fitness += i;
	    
	    
	    for(int i=0;i<this.sample_size;i++)
	    	this.prob_selection.add(this.fitness.get(i)/total_fitness);

	}

   /*       Selecting the sample on the basis of probability - "Roulette Wheel Selection"   followed by Crossover    */
	
	public List<String> selection() {
		List<String> children = new ArrayList<String>();
		int[] x = new int[this.sample_size];
		double[] d = new double[this.sample_size]; 
		for(int i=0;i<this.sample_size;i++) {
			x[i]=i;
			d[i]=this.prob_selection.get(i);
			
		}
		
		// EnumeratedIntegerDistribution = Generating random number of the basis of given Probability
		
		EnumeratedIntegerDistribution dist   = new EnumeratedIntegerDistribution(x,d);
		for(int i=0;i<this.sample_size/2;i++)
		{
			double rnd = getRandomNumber(0,1);
			int pidx1 = dist.sample();
			int pidx2 = dist.sample();
			
			if(rnd>this.prob_cross){
				children.add(this.population.get(pidx1));
				children.add(this.population.get(pidx2));
			}
			else {
				String[] child_aftCross = this.crossover(this.population.get(pidx1),this.population.get(pidx2));
				children.add(child_aftCross[0]);
				children.add(child_aftCross[1]);
			}
		}
		return children;
	}
	
	/*       Single Point Crossover between two samples       */
	
	public String[] crossover(String par1,String par2) {
		String[] res = new String[2];
		
		int rnd_cite = getRandomInteger(1,this.soln_len-1);
		
		String temp11 = par1.substring(0,rnd_cite);
		String temp21 = par2.substring(0,rnd_cite);
		String temp12 = par1.substring(rnd_cite,this.soln_len); 
		String temp22 = par2.substring(rnd_cite,this.soln_len); 
		
		
		res[0] = temp11 + temp22;
		res[1] = temp21 + temp12;
		
		
		
		return res;
	}
	
	/*       Bit Flip Mutation on sample       */
	
	public List<String> mutation(List<String> children) {
		
		int total_bits = this.soln_len * this.sample_size;
		
		int no_mutation = (int)(this.prob_muta*total_bits);
		
		for(int i=1;i<=no_mutation;i++)
		{
			int idx = getRandomInteger(0,total_bits-1);
			int soln_idx = (int)(idx/this.soln_len);
			int bit_idx = (int)(idx%this.soln_len);
			String temp = children.get(soln_idx);
			String child = temp.substring(0,bit_idx);
			if(temp.charAt(bit_idx)=='0')
				child+='1';
			else
				child+='0';
			child+=temp.substring(bit_idx+1,temp.length());
			
			children.set(soln_idx, child);
		}
		
		return children;
		
		
	}
	
	/*       Updating the Initial Population       */
	
	public void updatePopulation(List<String> children) {
		this.population = new ArrayList<String>(children);
		
	}
	
	
	/*       Getting the Result of the Algorithm       */
	
	
	public double[] getResult() {
		this.calculateFitness();
		double best_value = Collections.max(this.fitness);
		int best_idx = this.fitness.indexOf(best_value);
		
		String best = this.population.get(best_idx);
		int var_len = this.soln_len/this.dim;
		double values[] = new double[this.dim];
		int cnt=0; 
		for(int i=0;i<this.soln_len;i+=var_len) {
			String var = best.substring(i,i+var_len);
			int var_int = Integer.parseInt(var,2);
			
			double var_double = this.bounds[0] + ( ( ( this.bounds[1] - this.bounds[0] ) / ( ((int)Math.pow(2,var_len))-1) ) * var_int);
			
			
			values[cnt++]=var_double;
	
		}
		return values;
	}
	
	/*       Some Functions for generating random number of both Integer and Double Type       */
	
	public double getRandomNumber(int min, int max) {
	    return ((Math.random() * (max - min)) + min);
	}
	
	public static int getRandomInteger(int Min, int Max)
    {
        return ThreadLocalRandom
            .current()
            .nextInt(Min, Max + 1);
    }
	
	
}
