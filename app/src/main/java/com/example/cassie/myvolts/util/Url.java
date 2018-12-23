package com.example.cassie.myvolts.util;

public class Url {
	/**
	 * set url including ip address for http request
	 */
	public static String manu_url(){
		return "http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/29.03mv2.4/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0A%0ASELECT%20%3Fmanufacturer%20%3Fmname%20%20WHERE%20%7B%0A%3Fmanufacturer%20%3AManufacturer_name%20%3Fmname%20.%0A%7D&limit=100&infer=true&";
	}

	public static String pname_url(){
		return "http://api.myjson.com/bins/1hcph0";
//		return "http://theme-e.adaptcentre.ie/openrdf-workbench/repositories/29.03mv2.4/query?action=exec&queryLn=SPARQL&query=PREFIX%20%20%3A%20%3Chttp%3A%2F%2Fmyvolts.com%23%3E%0APREFIX%20owl%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX%20rdf%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX%20rdfs%3A%20%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0A%0ASELECT%20%3Fproduct%20%20%3Fpname%20%20WHERE%20%7B%0A%3Fproduct%20%3AProduct_name%20%20%3Fpname%20.%0A%7D&limit=100&infer=true&";
	}

}

