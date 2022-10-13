package com.algo.algo;
import java.lang.*; 
import java.util.*;



public class driver{
	public static void main(String[] args) {
		
		System.out.println("Genetic Algorithm (Binary Coded) \n");
		GAfunc();
		System.out.println();
		
		System.out.println("Differential Evolution \n");
		DEfunc();
		System.out.println();
	
		
		System.out.println("Artificial Bee Colony \n");
		ABCfunc();
		System.out.println();
		
		System.out.println("Particle Swarm Optimization \n");
		PSOfunc();
		System.out.println();
		
		

	}
	//Genetic Algorithm
	public static void GAfunc() {
		
		int dim =0;
		double prob_cross = 0.5;
		double prob_mutation = 0.05;
		int child_len=30;
		int sample_size =500;
		int generation = 2000;
	
		
		System.out.println("Number of Samples : " + sample_size);
		System.out.println("Total Number of Generation : " + generation);
		System.out.println("Number of Bits per Solution : " + child_len);
		System.out.println("Probability of Crossover : " + prob_cross);
		System.out.println("Probability of Mutation : " + prob_mutation);
		
		System.out.println();
		
		
		
		//Rastrigin Function 
		dim=3;
		System.out.println("Rastrigin Function :  \n");
		System.out.println("\t Number of Depending Variables for Rastrigin Function : " + dim);
		EvaluationGA(dim,new double[] {-5.12,5.12},prob_cross,prob_mutation,child_len,sample_size,generation,'R');
				
		//Ackley Function 
		dim=2;
		System.out.println("Ackley Function :  \n");
		System.out.println("\t Number of Depending Variables for Ackley Function : " + dim);
		EvaluationGA(dim,new double[] {-5,5},prob_cross,prob_mutation,child_len,sample_size,generation,'A');
		
		
		//Sphere Function 
		dim=3;
		System.out.println("Sphere Function :  \n");
		System.out.println("\t Number of Depending Variables for Sphere Function : " + dim);
		EvaluationGA(dim,new double[] {-1000,1000},prob_cross,prob_mutation,child_len,sample_size,generation,'S');
		
		//Rosenbrock Function 
		dim=2;
		System.out.println("Rosenbrock Function :  \n");
		System.out.println("\t Number of Depending Variables for Rosenbrock Function : " + dim);
		EvaluationGA(dim,new double[] {-10,10},prob_cross,prob_mutation,child_len,sample_size,generation,'B');
		
	}
	
	public static void EvaluationGA(int dim,double[] bounds,double prob_cross,double prob_mutation,int child_len,int sample_size,int generation,char typeFunc) {
		
		Function func = new Function(dim);
		 
		GA ga = new GA(func, dim,sample_size,bounds,prob_cross,prob_mutation,child_len,typeFunc);
		
		ga.initializePopulation();
		
		for(int i=1;i<=generation;i++)
		{
			ga.calculateFitness();
			
			ga.calculateProbRoulette();
			
			List<String> children = ga.selection();
			
			List<String> mutated_children = ga.mutation(children);
			
			ga.updatePopulation(mutated_children);
			
			
			
		}
		
		double values[] = ga.getResult();
		
		double val = func.eval(values,typeFunc);

		System.out.println("\t Function variable : "+Arrays.toString(values));
		System.out.println("\t Optimal Function Value : "+val);
		System.out.print("\n");
		
		
	}
	
	
	//Differential Evolution
	public static void DEfunc() {
		
		int dim;
		int sample_size = 100;
		int generation = 1000;
		double prob_recomb = 0.5;
		double beta = 0.5;
		
		System.out.println("Number of Samples : " + sample_size);
		System.out.println("Total Number of Generation : " + generation);
		System.out.println("Probability of Recombination : " + prob_recomb);
		System.out.println("Beta Value : " + beta);
		
		System.out.println();
		
		
		
		
		//Rastrigin Function 
		dim=2;
		System.out.println("Rastrigin Function :  \n");
		System.out.println("\t Number of Depending Variables for Rastrigin Function : " + dim);
		EvaluationDE(dim,new double[] {-5.12,5.12},prob_recomb,beta,800,2000,'R');
						
		//Ackley Function 
		dim=2;
		System.out.println("Ackley Function :  \n");
		System.out.println("\t Number of Depending Variables for Rastrigin Function : " + dim);
		EvaluationDE(dim,new double[] {-5.0,5.0},prob_recomb,beta,500,2000,'A');
				
				
		//Sphere Function 
		dim=2;
		System.out.println("Sphere Function :  \n");
		System.out.println("\t Number of Depending Variables for Rastrigin Function : " + dim);
		EvaluationDE(dim,new double[] {-100.0,100.0},prob_recomb,beta,900,1000,'S');
				
		//Rosenbrock Function 
		dim=2;
		System.out.println("Rosenbrock Function :  \n");
		System.out.println("\t Number of Depending Variables for Rastrigin Function : " + dim);
		EvaluationDE(dim,new double[] {-10.0,10.0},prob_recomb,beta,1000,10000,'B');
		
		
		
		
	}
	
	public static void EvaluationDE(int dim,double[] bounds,double prob_recomb,double beta,int sample_size,int generation,char typeFunction) {
		
		Function func = new Function(dim);
		
		DE de = new DE(func,bounds,dim,sample_size,prob_recomb,beta,typeFunction);
		
		de.initializePopulation();
		
		while(generation-- !=0)
		{
			
			
			int bestFitIdx = de.calculateFitness();
			
			de.reproductionPopulationUpdate(bestFitIdx);

			
		}
		List<Double> res = de.result();
		
		double val = func.eval(de.conversionListToArray(res),typeFunction);

		System.out.println("\t Function variable : "+res);
		System.out.println("\t Optimal Function Value : "+val);
		System.out.print("\n");
		
		
	}
	
	
	//Artificial Bee Colony
	public static void ABCfunc() {
		
		int dim;
		int swarmVariable = 1000;
		int generation = 10000;
		double phi = 0.4;
		
		
		
		System.out.println("Total Swarm Variable : " + swarmVariable);
		System.out.println("Total Number of Generation : " + generation);
		System.out.println("Phi value : " + phi);
		
		System.out.println();
	
	
		
		//Rastrigin Function 
		dim=4;
		System.out.println("Rastrigin Function :  \n");
		System.out.println("\t Number of Depending Variables for Rastrigin Function : " + dim);
		EvaluationABC(new double[] {-5.12,5.12},dim,swarmVariable,phi,'R',generation);
						
		//Ackley Function 
		dim=2;
		System.out.println("Ackley Function :  \n");
		System.out.println("\t Number of Depending Variables for Ackley Function : " + dim);
		EvaluationABC(new double[] {-5.0,5.0},dim,swarmVariable,phi,'A',generation);
				
				
		//Sphere Function
		dim=4;
		System.out.println("Sphere Function :  \n");
		System.out.println("\t Number of Depending Variables for Sphere Function : " + dim);
		EvaluationABC(new double[] {-100.0,100.0},dim,swarmVariable,phi,'S',generation);
				
		//Rosenbrock Function 
		dim=4;
		System.out.println("Rosenbrock Function :  \n");
		System.out.println("\t Number of Depending Variables for Rosenbrock Function : " + dim);
		EvaluationABC(new double[] {-100.0,100.0},dim,swarmVariable,phi,'B',generation); 
		
		
	}
	
	public static void EvaluationABC(double[] bounds,int dim,int swarm_var,double phi,char typeFunction, int generation) {
		
		Function func = new Function(dim);
		
		ABC abc = new ABC(func,bounds,dim,swarm_var,phi,typeFunction);
		
		abc.initializePopulation();
		
		abc.calculateFitness();
		int t=1;
		List<Double> best_parent = new ArrayList<>();
		while(t<=generation) {
			abc.employeedBeePhase();
			abc.probCalculator();
			abc.onLookerBeePhase();
			best_parent = abc.memorizeBestSol();
			abc.scoutBeePhase();
			t++;
		}
		
		double funcVal = func.eval(abc.conversionListToArray(best_parent), typeFunction);
	
		System.out.println("\t Function variable : "+best_parent);
		System.out.println("\t Optimal Function Value : "+funcVal);
		System.out.print("\n");
		
		
	}

	
	//Particle Swarm Optimization
	public static void PSOfunc() {
		
		int dim;
		int sample_size = 200;
		int generation = 1000;
		double w = 0.7;
		double c1 = 1.5;
		double c2 = 1.5;
		
		
		System.out.println("Total Sample Size : " + sample_size);
		System.out.println("Total Number of Generation : " + generation);
		System.out.println("w : " + w);
		System.out.println("c1 : " + c1);
		System.out.println("c2 : " + c2);
		
		System.out.println();
		
		
		
		//Rastrigin Function 
		dim=4;
		System.out.println("Rastrigin Function :  \n");
		System.out.println("\t Number of Depending Variables for Rastrigin Function : " + dim);
		EvaluationPSO(new double[] {-5.12,5.12},dim,sample_size,generation,w,c1,c2,'R');
						
		//Ackley Function 
		dim=2;
		System.out.println("Ackley Function :  \n");
		System.out.println("\t Number of Depending Variables for Ackley Function : " + dim);
		EvaluationPSO(new double[] {-5.0,5.0},dim,sample_size,generation,w,c1,c2,'A');
				
				
		//Sphere Function
		dim=4;
		System.out.println("Sphere Function :  \n");
		System.out.println("\t Number of Depending Variables for Sphere Function : " + dim);
		EvaluationPSO(new double[] {-100.0,100.0},dim,sample_size,generation,w,c1,c2,'S');
				
		//Rosenbrock Function 
		dim=4;
		System.out.println("Rosenbrock Function :  \n");
		System.out.println("\t Number of Depending Variables for Rosenbrock Function : " + dim);
		EvaluationPSO(new double[] {-10.0,10.0},dim,sample_size,generation,w,c1,c2,'B');
		
		
	}
	
	public static void EvaluationPSO(double[] bounds,int dim,int sample_size,int generation,double w,double c1,double c2,char typeFunction) {
		
		Function func = new Function(dim);
		
		PSO pso = new PSO(func,bounds,dim,sample_size,w,c1,c2,typeFunction);
		
		pso.initializePopulation();
		
		pso.calFitnessInitBest();
		
		while(generation-- !=0) {
			
			pso.generateSolution();
			
			
		}
		
		
		System.out.println("\t Function variable : "+pso.bestGloPosition);
		System.out.println("\t Optimal Function Value : "+pso.gloFitness);
		System.out.print("\n");
		
	
	}
	
	
	

}
