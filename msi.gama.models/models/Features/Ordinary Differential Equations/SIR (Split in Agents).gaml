/**
 *  SIR_split_in_agents.gaml
 *  Author: tri and nghi
 *  Description: 
 *  This model illustrates the possibility to split an equation system into several agents. 
 *  All the equations are solve together thanks to the `simultaneously` facet of the equation statement. 
 *  We also compare the split model with the simple SIR one. 
 */
 
model SIR_split_in_agents

global {
	int number_S <- 495 ; // The number of susceptible
	int number_I <- 5   ; // The number of infected
	int number_R <- 0   ; // The number of removed 

	float beta  <- 1.0  ; // The parameter Beta
	float delta <- 0.01 ; // The parameter Delta
	
	// Note that N will remain constant as demography is not taken into account in the SIR model.
	int N <- number_S + number_I + number_R ;
	float hKR4 <- 0.07 ;

	init {
		create S_agt {
			Ssize <- float(number_S) ;
			self.beta <- myself.beta ;
		}
		create I_agt {
			Isize <- float(number_I) ;
			self.beta <- myself.beta ;
			self.delta <- myself.delta ;
		}
		create R_agt {
			Rsize <- float(number_R) ;
			self.delta <- myself.delta ;
		}
		        
		create SIR_agt {
			self.Sm <- float(number_S) ;
			self.Im <- float(number_I) ;
			self.Rm <- float(number_R) ;
			
			self.beta <- myself.beta ;
			self.delta <- myself.delta ;
		}
	}
}


species S_agt {
	float t ;		
	float Ssize ;
	
	float beta ;
	
	equation evol simultaneously: [ first ( I_agt ) , first ( R_agt ) ] {
		diff ( first ( S_agt ) . Ssize , t ) = 
			( - beta * first ( S_agt ) . Ssize * first (	I_agt ) . Isize / N ) ;
	}
	
	reflex solving {solve evol method : "rk4" step : 0.01 ;}
}

species I_agt {
	float t ;
	float Isize ; // number of infected
	
	float beta ;
	float delta ;

	equation evol simultaneously : [ first ( S_agt ) , first ( R_agt ) ] {
		diff ( first ( I_agt ) . Isize , t ) = 
			( beta * first ( S_agt ) . Ssize * first ( I_agt ) . Isize / N ) 
			- ( delta * first ( I_agt ) . Isize ) ;
	}
}

species R_agt {
	float t ;		
	float Rsize ;
	
	float delta ;

	equation evol simultaneously : [ first ( S_agt ) , first ( I_agt ) ] {
		diff ( first ( R_agt ) . Rsize , t ) = 
			( delta * first ( I_agt ) . Isize ) ;
	}
}

species SIR_agt {
	float t ;
	float Im ;
	float Sm ;
	float Rm ;
	
	float beta ;
	float delta ;
	
	equation SIR {
		diff ( Sm , t ) = ( - beta * Sm * Im / N ) ; 
		diff ( Im , t ) = ( beta * Sm	* Im / N ) - ( delta * Im ) ; 
		diff ( Rm , t ) = ( delta * Im ) ;
	}
	
	reflex solving {solve SIR method : "rk4" step : 0.01 ;}
}


experiment Simulation type : gui {
	parameter 'Number of Susceptible' type: int var: number_S <- 495 category: "Initial population"; // The initial number of susceptibles
	parameter 'Number of Infected'    type: int var: number_I <- 5   category: "Initial population";
	parameter 'Number of Removed'     type: int var: number_R <- 0   category: "Initial population";

	parameter 'Beta (S->I)'  type: float var: beta <- 1.0   category: "Parameters";
	parameter 'Delta (I->R)' type: float var: delta <- 0.01 category: "Parameters";
	
	output {
		display "split system" {
			chart 'Susceptible' type : series background : rgb ( 'lightGray' ) {
				data 'susceptible' value : first ( S_agt ) . Ssize color : rgb ( 'green' ) ;
				data 'infected' value : first ( I_agt ) . Isize color : rgb ( 'red' ) ;
				data 'removed' value : first ( R_agt ) . Rsize color : rgb ( 'blue' ) ;
			}
		}
		display "unified system"{
			chart 'Susceptible' type : series background : rgb ( 'lightGray' ) {
				data 'susceptible_maths' value : first( SIR_agt ).Sm color : rgb ( 'green' ) ;
				data 'infected_maths' value : first( SIR_agt ).Im color : rgb ( 'red' ) ;
				data 'removed_maths' value : first( SIR_agt ).Rm color : rgb ( 'blue' ) ;
			}
		}
	}
}
