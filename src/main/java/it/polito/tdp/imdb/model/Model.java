package it.polito.tdp.imdb.model;

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
	SimpleWeightedGraph <Actor, DefaultWeightedEdge> grafo;
	Map <Integer, Actor> idMap;
	public Model() {
		dao= new ImdbDAO();
		idMap= new HashMap <Integer, Actor>();
		dao.listAllActors(idMap);
	}
	
	public List<String> listAllGenres(){
		return dao.listAllGenres();
	}
	
	public void creaGrafo(String genere) {
		grafo= new SimpleWeightedGraph <Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, dao.getVertici(genere, idMap));
		for(Adiacenza a: dao.getAdiacenze(genere, idMap)) {
			if(grafo.containsVertex(a.getA1())&& grafo.containsVertex(a.getA2())) {
				Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
	
	}
	
	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}

	public ImdbDAO getDao() {
		return dao;
	}

	public SimpleWeightedGraph<Actor, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public Map<Integer, Actor> getIdMap() {
		return idMap;
	}
	
	public List <Actor> trovaRaggiungibili (Actor partenza){
		List <Actor> percorso= new LinkedList <Actor>();
		
		BreadthFirstIterator <Actor, DefaultWeightedEdge> bfv= new BreadthFirstIterator <Actor, DefaultWeightedEdge>(this.grafo, partenza);
		while(bfv.hasNext()) {
			Actor c= bfv.next();
			if(!c.equals(partenza))
				percorso.add(c);
		}
		Collections.sort(percorso);
		return percorso;
	}
	
	public String Simula(Integer N, String genere, Map <Integer, Actor>idMap) {
		Simulatore sim= new Simulatore(grafo, this);
		sim.init(N, genere, idMap);
		sim.run();
		String messaggio="Sono stati intervistati questi attori: \n";
		for(Actor a: sim.getIntervistati()) {
			messaggio= messaggio+a.toString()+"\n";
		}
		messaggio=messaggio+" con un totale di"+ sim.getGiorniPausa()+" giorni di Pausa";
		return messaggio;
		
	}

}
