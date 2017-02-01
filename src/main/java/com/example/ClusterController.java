package com.example;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import weka.clusterers.Canopy;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.JSONSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddCluster;
import weka.filters.unsupervised.attribute.NumericToNominal;

@RestController
public class ClusterController {

	/* initalizes class variables */
	private static File outputFileClusters;
	private static File outputFileClusterAssignments;
	private static CSVLoader loader;
	private static Instances data;
	private static Instances newData;
	private static String[] clusteringOptions;
	private static String[] filteringOptions;
	private static Canopy clusterer;
	private static JSONSaver saver;
	private static NumericToNominal convert;
	private static AddCluster add;
	private static File file;
	private static Cluster testCluster;
	private static ArrayList<Cluster> cluster = new ArrayList<Cluster>();

	@RequestMapping(value = "/cluster")
	public ClusterList cluster(@RequestBody RequestClass request) throws Exception {
		
		/*String test = "ident,dayOfWeek,weekOfYear,startingTime,endingTime,long,lat \n "
				+ "Fussballtraining,0,0,18,20,49.085947,12.248254 \n "
				+ "Fussballtraining,0,1,18,20,49.085947,12.248254 \n "
				+ "Fussballtraining,0,0,18,20,49.085947,12.248254 \n "
				+ "Theaterprobe,1,0,15,19,47.085947,9.248254 \n "
				+ "Theaterprobe,1,0,15,19,47.085947,9.248254 \n "
				+ "Theaterprobe,1,0,15,19,47.085947,9.248254 \n"
				+ "Theaterprobe,1,0,15,19,47.085947,9.248254"; */ 

		
		System.out.println(request.name);
		loadCSVFile(request.name);
		getInstancesFromCSV();
		editCSVAttributes();
		defineClusteringOptions();
		executeClustering();
		executeFilterOnTestData();
		
		 
		for(int i = 0; i < clusterer.getCanopies().size(); i++) {
		
		String cluster1 = clusterer.getCanopies().get(i).toString();
		String[] parts = cluster1.split(",");
		String part1 = parts[0];
		
		
		String part2 = parts[1];
		int dayOfWeek = Integer.parseInt(part2);
		
		String part3 = parts[2];
		int weekOfYear = Integer.parseInt(part3);
		
		String part4 = parts[3];
		double startingTime = Double.parseDouble(part4);
		
		String part5 = parts[4];
		double endingTime = Double.parseDouble(part5);

		
		String part6 = parts[5];
		double longitude = Double.parseDouble(part6);

		
		String part7 = parts[6];
		double latitude = Double.parseDouble(part7);
		
		testCluster = new Cluster(part1, dayOfWeek, weekOfYear, startingTime, endingTime, longitude, latitude);
		cluster.add(testCluster);
		
		}
		ClusterList clusterList = new ClusterList();
		clusterList.clusterList=cluster;
		
		return clusterList;


		

	}
	
	/* Method: executeFilterOnTestData() */
	/**
	 * applies filter on test data.
	 */
	private static void executeFilterOnTestData() throws Exception {
		add = new AddCluster();
		add.setClusterer(clusterer);
		add.setInputFormat(newData);
		newData = Filter.useFilter(newData, add);

	}

	/* Method: loadCSVFile() */
	/**
	 * initiates the csv loader from the weka.jar file and sets its source to
	 * the delivered csv file.
	 * 
	 * @throws IOException
	 */
	private static void loadCSVFile(String name) throws IOException {
		loader = new CSVLoader();
		InputStream stream = new ByteArrayInputStream(name.getBytes("UTF-8"));
		loader.setSource(stream);

	}

	/* Method: getInstancesFromCSV() */
	/**
	 * puts the data from the csv-loader into an instances object.
	 * 
	 * @throws IOException
	 */
	private static void getInstancesFromCSV() throws IOException {
		data = loader.getDataSet();
	}

	/* Method: editCSVAttributes() */
	/**
	 * edits the csv attributes (numeric --> nominal)
	 * 
	 * @throws Exception
	 */
	private static void editCSVAttributes() throws Exception {
		convert = new NumericToNominal();
		defineFilteringOptions();
		applyFilter();

	}

	/* Method: applyFilter() */
	/**
	 * sets the filtering options and adds the cluster assignment to the test
	 * data.
	 */
	private static void applyFilter() throws Exception {
		convert.setOptions(filteringOptions);
		convert.setInputFormat(data);
		newData = Filter.useFilter(data, convert);

	}

	/* Method: defineFilteringOptions() */
	/**
	 * initiates string array with the needed filtering options to convert
	 * numeric attributes to nominal attributes. Valid options can be found
	 * here:
	 * http://weka.sourceforge.net/doc.stable/weka/filters/unsupervised/attribute/NumericToNominal.html
	 */
	private static void defineFilteringOptions() throws Exception {
		filteringOptions = new String[2];
		filteringOptions[0] = "-R";
		filteringOptions[1] = "2-3";

	}
	
	/* Method: defineClusteringOptions() */
	/**
	 * initializes a string array and sets the options for the canopy
	 * clustering. Valid options can be found here:
	 * http://weka.sourceforge.net/doc.dev/weka/clusterers/Canopy.html
	 */
	private static void defineClusteringOptions() {
		clusteringOptions = new String[2];
		clusteringOptions[0] = "-N";
		clusteringOptions[1] = "-1";

	}

	/* Method: executeClustering() */
	/**
	 * initiates a canopy clusterer, sets the options and builds the resulting
	 * clusters.
	 * 
	 * @throws Exception
	 */
	private static void executeClustering() throws Exception {
		clusterer = new Canopy();
		clusterer.setOptions(clusteringOptions);
		clusterer.buildClusterer(newData);

	}
}
