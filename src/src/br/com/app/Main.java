package br.com.app;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.vocabulary.OWL;

public class Main {

	public static void main(String[] args) {

		String NS = "urn:x-hp:eg/";
		final String ruleSwrl = "[S1: (?x eg:hasSon ?y), (?y eg:hasSon ?z), (?w eg:hasSpouse ?y) -> (?z eg:hasGrandFather ?x), (?w eg:hasSon ?z), (?w rdf:type Person)]";

		OntModel ontModel = ModelFactory.createOntologyModel();
		ontModel.createOntology("http://www.example.com.br/onto-jena/");

		OntClass person = ontModel.createClass(NS + "Person");

		Individual joao = person.createIndividual(NS + "Joao");
		Individual maria = person.createIndividual(NS + "Maria");
		Individual jose = person.createIndividual(NS + "Jose");
		Individual carlos = ontModel.createIndividual(NS + "Carlos", OWL.Thing);

		ObjectProperty hasSon = ontModel.createObjectProperty(NS + "hasSon");
		ontModel.createObjectProperty(NS + "hasGrandFather");
		ObjectProperty hasSpouse = ontModel.createObjectProperty(NS + "hasSpouse");

		ontModel.add(joao, hasSon, maria);
		ontModel.add(maria, hasSon, jose);
		ontModel.add(carlos, hasSpouse, maria);

		Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(ruleSwrl));
		reasoner.setDerivationLogging(true);

		InfModel inf = ModelFactory.createInfModel(reasoner, ontModel);

		System.out.println(
				"---------------------------------Inference of José-----------------------------------------------");
		printInference(inf, jose);
		System.out.println("\n");

		System.out.println(
				"---------------------------------Inference of Carlos-----------------------------------------------");
		printInference(inf, carlos);
		System.out.println("\n");

		System.out
				.println("---------------------------------RDF Turtle-----------------------------------------------");
		inf.write(System.out, "Turtle");
	}

	public static void printInference(InfModel inf, Individual individual) {
		StmtIterator stmts = inf.listStatements(individual, (ObjectProperty) null, (RDFNode) null);

		while (stmts.hasNext()) {
			Statement stmt = stmts.nextStatement();
			Resource subject = stmt.getSubject();
			Property predicate = stmt.getPredicate();
			RDFNode object = stmt.getObject();

			System.out.print(subject.getLocalName());
			System.out.print(" " + predicate.getLocalName() + " ");
			if (object instanceof Resource) {
				System.out.print(((Resource) object).getLocalName());
			} else {
				System.out.print(" \"" + object.asLiteral().toString() + "\"");
			}

			System.out.println(".");
		}
	}
}
