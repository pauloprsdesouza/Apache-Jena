package sparql;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.expr.E_LessThan;
//import com.hp.hpl.jena.sparql.expr.
import com.hp.hpl.jena.sparql.expr.Expr;
import com.hp.hpl.jena.sparql.expr.ExprVar;
import com.hp.hpl.jena.sparql.expr.nodevalue.NodeValueInteger;
import com.hp.hpl.jena.sparql.syntax.ElementFilter;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.syntax.ElementTriplesBlock;

public class Teste
{
	public static void main(String[] args)
	{
		
		FirstMethod();
		//SecondMethod();
		//ThirdMethod();
		//FourthMethod();
	}
	
	//  querying and processing results of a remote SPARQL endpoint
	
	private static void FirstMethod()
	{
		String queryString = 
		"PREFIX dct: <http://purl.org/dc/terms/>" + 
		"		SELECT ?car\n"+
		"		where {\n"+
		"		?car dct:subject <http://dbpedia.org/resource/Category:Luxury_vehicles>\n" +
		"		}\n" +
		"ORDER by ?car";
		
		// now creating query object
		Query query = QueryFactory.create(queryString);
		
		// initializing queryExecution factory with remote service.
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

		//after it goes standard query execution and result processing which can
		// be found in almost any Jena/SPARQL tutorial.
		
		try
		{
			ResultSet results = qexec.execSelect();
			
			// Output query results    
			ResultSetFormatter.out(System.out, results, query);
		}
		finally
		{
			qexec.close();
		}
	}
	
	// SPARQL from inside Jena
	
	private static void SecondMethod()
	{
		String SOURCE = "C:/Users/Paulo Roberto/Documents/Projetos Java/JenaProject.zip_expanded/JenaProject/JenaProject/src/sparql/JenaInf.owl";
        String NS = SOURCE + "#";
       //create a model using reasoner
        OntModel model1 = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF);
       //create a model which doesn't use a reasoner
        OntModel model2 = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM);
        
        // read the RDF/XML file
        model1.read( SOURCE, "RDF/XML" );
        model2.read( SOURCE, "RDF/XML" );
        //prints out the RDF/XML structure
        //qe.close();
        System.out.println(" ");
        

		// Create a new query
		String queryString =        
		  "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
		    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  "+
		    "select ?uri "+
		    "where { "+
		     "?uri rdfs:subClassOf <http://www.opentox.org/api/1.1#Feature>  "+
		    "} \n ";
		Query query = QueryFactory.create(queryString);
		
		System.out.println("----------------------");
		
		System.out.println("Query Result Sheet");
		
		System.out.println("----------------------");
		
		System.out.println("Direct&Indirect Descendants (model1)");
		
		System.out.println("-------------------");
		
		   
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model1);
		com.hp.hpl.jena.query.ResultSet results =  qe.execSelect();
		
		// Output query results    
		ResultSetFormatter.out(System.out, results, query);
		
		qe.close();
		
		System.out.println("----------------------");
		System.out.println("Only Direct Descendants");
		System.out.println("----------------------");
		
		// Execute the query and obtain results
		qe = QueryExecutionFactory.create(query, model2);
		results =  qe.execSelect();
		
		// Output query results    
		ResultSetFormatter.out(System.out, results, query);  
		qe.close();
	}
	
	private static void ThirdMethod()
	{
		// ?s ?p ?o .
		Triple pattern = Triple.create(Var.alloc("Filmes"),Var.alloc("p"),Node.createURI("http://dbpedia.org/ontology/Film"));
		// ( ?s < 20 )
		//Expr e = new E_LessThan(new ExprVar("s"), new NodeValueInteger(20));
	   
		ElementTriplesBlock block = new ElementTriplesBlock();
		block.addTriple(pattern);
		//ElementFilter filter = new ElementFilter(e);
		ElementGroup body = new ElementGroup();
		body.addElement(block);
		//body.addElement(filter);

		Query q = QueryFactory.make();
		q.setQueryPattern(body);
		q.setQuerySelectType();
		q.setLimit(10);
		q.addResultVar("Filmes");
		
		System.out.println(q.toString());
		
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://pt.dbpedia.org/sparql", q);
		
		try
		{
			ResultSet results = qexec.execSelect();
			ResultSetFormatter.out(System.out, results, q);
		}
		finally
		{
			qexec.close();
		}		
	}
	
	private static void FourthMethod()
	{
		// ?s ?p ?o .
		Triple pattern = Triple.create(Var.alloc("s"),Node.createURI("http://dbpedia.org/ontology/populationTotalRanking"),Var.alloc("p"));
		// ( ?s < 100 )
		Expr e = new E_LessThan(new ExprVar("p"), new NodeValueInteger(100));
	   
		ElementTriplesBlock block = new ElementTriplesBlock();
		block.addTriple(pattern);
		ElementFilter filter = new ElementFilter(e);
		ElementGroup body = new ElementGroup();
		body.addElement(block);
		body.addElement(filter);

		Query q = QueryFactory.make();
		q.setQueryPattern(body);
		q.setQuerySelectType();
		q.addResultVar("s");
		q.addResultVar("p");
		
		System.out.println(q.toString());
		
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://pt.dbpedia.org/sparql", q);
		
		try
		{
			ResultSet results = qexec.execSelect();
			ResultSetFormatter.out(System.out, results, q);
		}
		finally
		{
			qexec.close();
		}		
	}
}
