package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;


import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	ImdbDAO dao;
	Map <Integer, Actor> idMap;
	SimpleWeightedGraph <Actor, DefaultWeightedEdge> grafo;
	public Model() {
		dao= new ImdbDAO();
		idMap= new HashMap<>();
		dao.listAllActors(idMap);
	}
	
	
	public void creaGrafo(String genere) {
		grafo= new SimpleWeightedGraph <Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, dao.getVertici(idMap, genere));
		for(Adiacenza a: dao.getAdiacenza(idMap, genere)) {
			if(grafo.containsVertex(a.getA1())&&grafo.containsVertex(a.getA2())) {
				Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
	
	}
	public String simula(int giorni) {
		Simulatore sim= new Simulatore(this, this.grafo);
		sim.init(giorni);
		sim.run();
		String result="Attori intervistati: \n";
		for(Actor a: sim.getIntervistati()) {
			result= result+a.toString()+"\n";
			
		}
		result=result+"Numero giorni di pausa: "+sim.getPause();
		return result;
	}
	public ImdbDAO getDao() {
		return dao;
	}


	public Map<Integer, Actor> getIdMap() {
		return idMap;
	}


	public SimpleWeightedGraph<Actor, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}


	public List <Actor> raggiungibili (Actor selezionato){
		List<Actor> raggiungibili= new ArrayList<>();
		BreadthFirstIterator <Actor, DefaultWeightedEdge> bfv= new BreadthFirstIterator<Actor, DefaultWeightedEdge>(grafo, selezionato);
		while(bfv.hasNext()) {
			raggiungibili.add(bfv.next());
		}
		Collections.sort(raggiungibili);
		return raggiungibili;
	}
	
	public int getNVertici() {
		return grafo.vertexSet().size();
	}
	public int getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public List <String> getGeneri(){
		return dao.getGeneri();
	}

}
