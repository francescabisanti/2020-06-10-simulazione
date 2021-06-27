package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Simulatore {
	//Parametri di input
	private Integer nGiorni;
	private List<Actor> attori;
	
	//Parametri di output
	List <Actor> intervistati;
	Integer giorniPausa;
	
	//Coda degli eventi
	
	
	//Stato del mondo
	private SimpleWeightedGraph <Actor, DefaultWeightedEdge> grafo;
	Model model;
	Integer time;
	
	public Simulatore(SimpleWeightedGraph <Actor, DefaultWeightedEdge> grafo, Model model) {
		this.grafo=grafo;
		this.model=model;
	}
	
	public void init(Integer N, String genere, Map <Integer, Actor> idMap) {
		this.nGiorni=N;
		
		this.giorniPausa=0;
		this.time=0;
		this.intervistati=new ArrayList <Actor>();
		this.attori= this.model.getDao().getVertici(genere, idMap);
		
		int index = (int) (Math.random() * attori.size());
		Actor partenza= attori.get(index);
		intervistati.add(partenza);
		
	}
	
	public void run() {
		while(time<=this.nGiorni) {
			if(Math.random()>0.4) {
				int index = (int) (Math.random() * attori.size());
				Actor successivo= attori.get(index);
				if(!intervistati.contains(successivo)) {
					intervistati.add(successivo);
					this.time++;
				}
				else {
					int index2 = (int) (Math.random() * attori.size());
					Actor successivo2= attori.get(index2);
					if(!intervistati.contains(successivo2)) {
						intervistati.add(successivo2);
						this.time++;
					}
				}
			}
			else if(Math.random()>0.6) {
				Actor ultimo=intervistati.get(intervistati.size()-1);
				int gradoMax=Integer.MIN_VALUE;
				Actor prossimo=null;
				for(Actor a : Graphs.neighborListOf(grafo, ultimo)) {
					if(grafo.degreeOf(a)>gradoMax) {
						prossimo=a;
						gradoMax=grafo.degreeOf(a);
					}
				}
				intervistati.add(prossimo);
				this.time++;
				
			}
			if(intervistati.size()>2 && intervistati.get(intervistati.size()-1).getGender()==intervistati.get(intervistati.size()-2).getGender()) {
				if(Math.random()>0.1) {
					this.giorniPausa++;
					this.time++;
				}
			}
		}
	}

	public List<Actor> getIntervistati() {
		return intervistati;
	}

	public Integer getGiorniPausa() {
		return giorniPausa;
	}
	
	

}
