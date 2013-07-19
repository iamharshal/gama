/**
 *  model3
 *  This model illustrates how to use spatial operator
 */ 
model model3 
 
global {
	file roads_shapefile <- file("../includes/road.shp");
	file buildings_shapefile <- file("../includes/building.shp");
	geometry shape <- envelope(roads_shapefile);
	graph road_network;
	init {
		create roads from: roads_shapefile;
		road_network <- as_edge_graph(roads);
		create buildings from: buildings_shapefile with: [type:: string(read("NATURE"))] {
			color <- type="Industrial" ? rgb("blue") : rgb("gray");
		}
		create people number:500 {
			target <- any_location_in(one_of(buildings where (each.type="Industrial" )).shape);
			list<buildings> residential_bg <- buildings where (each.type="Residential");
			buildings habitation <- residential_bg with_min_of (length(people inside each)/ each.shape.area);
			location <- any_location_in(habitation.shape);
		}
	}
}

species people skills:[moving]{		
	int size <- 5;
	float speed <- 5.0 + rnd(5);
	point target;
	reflex move{
		do goto target:target on: road_network ;
	}
	aspect circle{
		draw circle(size) color:rgb("green");
	}
}

species roads {
	aspect geom {
		draw shape color: rgb("black");
	}
}

species buildings {
	string type;
	rgb color;
	aspect geom {
		draw shape color: color;
	}
}

experiment main_experiment type:gui{
	output {
		display map {
			species roads aspect:geom;
			species buildings aspect:geom;
			species people aspect:circle;			
		}
	}
}