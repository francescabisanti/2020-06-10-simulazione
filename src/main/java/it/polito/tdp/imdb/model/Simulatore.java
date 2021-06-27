package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Simulatore {
	//Parametri di input
	int giorni;
	
	//parametri di output
	List<Actor> intervistati;
	int pause;
	
	//Coda degli eventi
	List<Actor> coda;
	
	//Stato del mondo
	Model model;
	List<Actor> tutti;
	SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo;
	
	
	public Simulatore(Model model,SimpleWeightedGraph<Actor, DefaultWeightedEdge> grafo) {
		this.model=model;
		this.grafo=grafo;
	}

	
	public void init(int giorni) {
		this.giorni=giorni;
		coda= new ArrayList<Actor>();
		intervistati= new ArrayList<Actor>();
		this.tutti= new ArrayList<>(this.grafo.vertexSet());
		this.pause=0;
		
	}
	
	public void run() {
		while(coda.size()<giorni) {
			if(intervistati.size()==0) {
				int indice= (int) (Math.random()*tutti.size());
				Actor a= tutti.get(indice);
				tutti.remove(a);
				intervistati.add(a);
				coda.add(a);
			}
			else {
			if(Math.random()>0.6) {
			int indice= (int) (Math.random()*tutti.size());
			Actor a= tutti.get(indice);
			tutti.remove(a);
			intervistati.add(a);
			coda.add(a);
			
			}
			else {
				Actor consigliere= intervistati.get(intervistati.size()-1);
				Actor a=null;
				int gradoMax=0;
				for(Actor aa: Graphs.neighborListOf(grafo, consigliere)) {
					if(grafo.degreeOf(aa)>gradoMax) {
						a=aa;
						gradoMax= grafo.degreeOf(a);
					}
				}
				if(a!=null) {
				intervistati.add(a);
				coda.add(a);}
				else {
					int indice= (int) (Math.random()*tutti.size());
					Actor casuale= tutti.get(indice);
					tutti.remove(casuale);
					intervistati.add(casuale);
					coda.add(casuale);
				}
			}
			}
			if(intervistati.size()>1 && intervistati.get(intervistati.size()-1).getGender().equals(intervistati.get(intervistati.size()-2).getGender())) {
				if(Math.random()>0.10) {
				this.pause++;
				this.coda.add(null);
				}
			}
			if(coda.get(coda.size()-1)==null) {
				int indice= (int) (Math.random()*tutti.size());
				Actor casuale= tutti.get(indice);
				tutti.remove(casuale);
				intervistati.add(casuale);
				coda.add(casuale);
				
			}
		}
	}


	public List<Actor> getIntervistati() {
		return intervistati;
	}


	public int getPause() {
		return pause;
	}
	
	
}
