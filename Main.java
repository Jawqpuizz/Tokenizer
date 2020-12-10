import java.util.*;
public class Main {
	 static String s;
	    static int curr_index;
	    static char input_token;
	    static boolean firstId;
	    static boolean firstLit;
	    static String assignment = "";// to keep each assignment for the output
	    static String var = "";// 
	    static boolean isVariable = false; 
	    static String varVal = "";
	    static String temp = "";//keep value of input as a string before covert it to integer
	    static Map<String, Integer> store = new HashMap<>();
	    
	//////////////Main function starts here//////////////////////////////////////  
	    public static void main(String[] args){
	    	// type input without the white space
	        s = "x_2=0;"+"$";
	        curr_index = 0;
	        next_token();
	        do {
	        firstId = true; isVariable = true;
	        identifier();
	        int value = exp();
	        semicolon();
	        saveData(var, value);// save each variable to HashMap 
	        exp_prime();
	        assignment = "";
	        }while(input_token != '$');
	        printOutput();
	    }

	    static void error(){
	        throw new RuntimeException("syntax error");
	    }

	    static void match(char expected_token){
	        if (input_token != expected_token)
	            error();
	        else
	        	assignment += input_token;
	            next_token();
	    }

	    static void next_token(){
	        if (curr_index >= s.length())
	            error();
	        input_token = s.charAt(curr_index++);
	    }

	    static int exp(){
	         if (Character.isDigit(input_token) || input_token == '(' || input_token == '-'
	        		|| input_token == '+' ||input_token == '_' || Character.isLetter(input_token)){
	        	 
		             int op1 = term();
		             if(input_token == '+') {
		             int op2 = op1 + exp_prime();
		             return op2;
		             }else if(input_token == '-'){
		             int op2 = op1 - exp_prime();	
		             return op2;
		             }else
		             return op1;
		        } else {
		            error();
		        }
	       return 0;
	    }

	    static int exp_prime(){
	    
	        if (input_token == '+'){	
	            match('+');                
	            return term() + exp_prime();
	         
	        } else if (input_token == '-'){
	            match('-');
	            return term() - exp_prime();
	            
	            
	        } else if (input_token == ')' || input_token == '$' || input_token == ';'){
	        	return 0;
	        } else {
	            error();
	        }
	  return 0;
	    }

	    static int term(){
	        if (Character.isDigit(input_token) || input_token == '(' || input_token == '-'
	        		|| input_token == '+' ||input_token == '_' || Character.isLetter(input_token)){
	        	 int op = factor();
	           return op * term_prime();
	             
	        } else {
	            error();
	        }
	       return 0;
	    }

	    static int term_prime(){
	    	 
	        switch (input_token){
	        case '*':
	            match('*');
	            int op = factor();
	            return op *= term_prime();
	        case '+':
	        case '-':
	        case ')':
	        case ';':
	        case '$':
	            return 1;
	        default:
	            error();
	        }
	        return 0;
	    }

	    static int factor(){
	    	//Fact:
	    	//	( Exp ) | - Fact | + Fact | Literal | Identifier	
	        if  (input_token == '('){ // (Exp)
	            match('(');
	            int op = exp();
	            match(')');
	            return op;
	        } else if(input_token == '-') {    //- Fact
	        	temp += input_token;
	        	 match('-');
	        	 return factor();
	        	 
	        } else if(input_token == '+') { // + Fact 
	        	temp += input_token;
	        	 match('+');
	        	 return factor();
	        }  else if(Character.isDigit(input_token)){// Literal
	        	if(input_token == '0') {
	        	match(input_token);	
	        	}else {
	        	firstLit = true;
	        	// set val back to blank    	
	        	literal(); 
	        	int litVal = Integer.parseInt(temp);
	        	temp = "";
	        	return litVal;
	        	}
	        }else if(Character.isLetter(input_token) || input_token == '_'){//Identifier    	
	        	firstId = true;
	        	varVal = "";
	        	identifier();
	        	// find value of a variable
	        	if(store.containsKey(varVal)) {
	        		return store.get(varVal);
	        	}else {
	        		throw new RuntimeException("variable " + varVal +" not found!!");
	        	}
	        
	        }
	      return 0;
	    }
	    static void literal() {
	    	// Literal:
	    	//0 | NonZeroDigit Digit* we check if it's 0 before going inside this function
	    	if(Character.isDigit(input_token)) temp += input_token;
	    	
	    	if(firstLit == true) {
	    		// first Literal character needs to be non-zero digit 
	    		if(Character.isDigit(input_token) && input_token != '0'){
	    			firstLit = false;
	    			match(input_token);
	    			literal();
	        	}
	    		
	    	}else {
	    		if(Character.isDigit(input_token)) {
	    			match(input_token);
	    			literal();
	    		}
	    	}
	    	
	    	
	    }
	  
	    static void identifier() {
	    		
	    	if(input_token != '=' && isVariable == true)   var += input_token;
	        
	    	if (firstId == true) {
	    		// first character of identifier can be a letter only
	    		//Identifier:
	         	//Letter [Letter | Digit]*
	    		
	    		if(Character.isLetter(input_token) || input_token == '_') {
 	    			firstId = false;
 	    			varVal += input_token;
	    			match(input_token);
	    			identifier();
	    			
	    		}else if (input_token == '$'){
	    			
	    		}else {
	    			throw new RuntimeException("Error: first id needs to be a letter or _ only");
	    		}
	    	}else {
	    		// can be only a letter or digit a|...|z|A|...|Z|_|0|...|9
	    		if(Character.isLetter(input_token) || input_token == '_'
	    			|| Character.isDigit(input_token)) {
	    			varVal += input_token;
	    			match(input_token);
	    			identifier();			
	    		}else if(input_token == '=') { 
	    			isVariable = false;
	    			match(input_token);		
	    		}else if(input_token == '$'|| input_token == '+' || input_token == '*' || input_token == '-'
	    				|| input_token == '(' ||input_token == ')' || input_token == ';'	) {
	    		}else {
	    			throw new RuntimeException("Error: Id can be only letter, '_', or digit");
	    		}
	    		
	    		
	    	}
	    	
	    }
	   
	     static void semicolon() {
	    	 if(input_token == ';') {
	    	 match(input_token);
	    	 }else {
	    		 throw new RuntimeException("Error: Missing ; (Semicolon)");
	    	 }
	     }
	     
	     static void saveData(String v, int value) {
	    	 if(store.containsKey(v)) {
	    		 store.put(v,store.get(v) + value);
	    	 }else {
	    		 store.put(v, value);
	    	 }
	    	 // set var to blank 
	    	 var = "";
	    	 
	     }
	     
	    static void printOutput() {
	    	// print data from hashmap 
	    	for(Map.Entry<String, Integer> set: store.entrySet()) {
	    		System.out.println(set.getKey() +" = "+set.getValue());
	    	}
	    	
	    }

}
