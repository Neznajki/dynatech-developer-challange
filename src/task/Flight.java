package task;

public class Flight {
	public String from;
	public String to;

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}
	
    @Override
    public boolean equals(Object o) { 
        // If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
        } 
  
        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof Flight)) { 
            return false; 
        } 
          
        // typecast o to Complex so that we can compare data members  
        Flight c = (Flight) o; 
          
        // Compare the data members and return accordingly  
        return this.getFrom().equals(c.getFrom()) && this.getTo().equals(c.getTo()) ;
    } 
}
