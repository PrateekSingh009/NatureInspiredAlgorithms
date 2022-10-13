package com.algo.algo;

public class Function {
	
	//Number of Depending Variable
	int dim;

	public Function(int dim) {
		this.dim=dim;
	}
	
	//Evaluating Function On the basis of type of Function
	/* R -> Rastrigin Function
	 * A -> Ackley Function
	 * S -> Sphere Function
	 * B -> Rosenbrock Function
	 * */
	
	
	public double eval(double values[],char typeFunction) {
		
		double sum = 0.0;
		
		
		if(typeFunction == 'R') {
			
			int A = 10;
			sum = A* this.dim;
		
			for(int i=1;i<=this.dim;i++)
			{
				sum += values[i-1] * values[i-1] - A * Math.cos( 2 * Math.PI * values[i-1] );
			}
		}
		
		
		if(typeFunction == 'A') {
			sum = -20 * Math.exp( -0.2 * Math.sqrt ( 0.5 * ( values[0]*values[0] + values[1]*values[1] ) ) ) 
					- Math.exp( 0.5 * ( Math.cos( 2 * Math.PI * values[0] )
					+ Math.cos( 2 * Math.PI * values[1] ) ) ) 
					+ Math.exp(1) 
					+ 20;
		}
		
		if(typeFunction == 'S') {
			for(int i=1;i<=this.dim;i++)
				sum += values[i-1]*values[i-1];
		}
		
		
		if(typeFunction == 'B') {
			
			for(int i=1;i<this.dim;i++)
			{
				sum += 100*( ( values[i]-  ( values[0] * values[0] ) ) *( values[i]-  ( values[0] * values[0] ) ) ) 
						+ ( ( 1 - values[0] )*( 1 - values[0] ) );
			}
			   
		}
		
		return sum;
	}
	
}
