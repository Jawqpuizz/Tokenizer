
public class Main {
	 static String s;
	    static int curr_index;
	    static char input_token;
	    static boolean firstId;
	    static boolean firstLit;
	    static String assignment = "";// to keep each assignment for the output
	    static String var = "";
	    static boolean isVariable = false;
	    static String temp ="";
	    static int val = 0;
	    static int op1 = 0;
	    static int op2 = 0;

	    public static void main(String[] args){
	    	// type input without the white space
	        s = "z=1+2;x=1;y=2;"+"$";
	        curr_index = 0;
	        next_token();
	        do {
	        firstId = true; isVariable = true;
	        identifier();
	        exp();
	        semicolon();
	        saveData();
	        System.out.println(assignment);
	        assignment = "";
	        }while(input_token != '$');
	      
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

	    static void exp(){
	         if (Character.isDigit(input_token) || input_token == '(' || input_token == '-'
	        		|| input_token == '+' ||input_token == '_' || Character.isLetter(input_token)){
	            term();
	            exp_prime();
	        } else {
	            error();
	        }
	    }

	    static void exp_prime(){
	        if (input_token == '+'){
	            match('+');
	         
	            term();
	            exp_prime();
	        } else if (input_token == '-'){
	            match('-');
	            term();
	            exp_prime();
	        } else if (input_token == ')' || input_token == '$' || input_token == ';'){
	        } else {
	            error();
	        }
	    }

	    static void term(){
	        if (Character.isDigit(input_token) || input_token == '(' || input_token == '-'
	        		|| input_token == '+' ||input_token == '_' || Character.isLetter(input_token)){
	            factor();
	            term_prime();
	        } else {
	            error();
	        }
	    }

	    static void term_prime(){
	        switch (input_token){
	        case '*':
	            match('*');
	            factor();
	            term_prime();
	            break;
	        case '+':
	        case '-':
	        case ')':
	        case ';':
	        case '$':
	            break;
	        default:
	            error();
	        }
	    }

	    static void factor(){
	    	//Fact:
	    	//	( Exp ) | - Fact | + Fact | Literal | Identifier	
	        if  (input_token == '('){ // (Exp)
	            match('(');
	            exp();
	            match(')');
	      
	        } else if(input_token == '-') {    //- Fact
	        	 match('-');
	        	 factor();
	       
	        } else if(input_token == '+') { // + Fact 
	        	 match('+');
	        	 factor();
	        }  else if(Character.isDigit(input_token)){// Literal
	        	if(input_token == '0') {
	        	match(input_token);	
	        	}else {
	        	firstLit = true;
	        	literal(); 	
	        	}
	        }else if(Character.isLetter(input_token) || input_token == '_'){//Identifier    	
	        	firstId = true;
	        	identifier();
	        
	        }
	    }
	    static void literal() {
	    	// Literal:
	    	//0 | NonZeroDigit Digit* we check if it's 0 before going inside this function
	    	if(Character.isDigit(input_token))temp += input_token;
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
	    			match(input_token);
	    			identifier();			
	    		}else if(input_token == '=') { 
	    			var =""; 
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
	     
	     static void saveData() {
	    	 
	     }

}
