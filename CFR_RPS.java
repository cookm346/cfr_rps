public class CFR_RPS{

    int regret[];
    int regret_sum[];
    double strategy[];
    double strategy_sum[];
    int N_MOVES = 3;
	private final int ROCK = 0, PAPER = 1, SCISSORS = 2;
    
    CFR_RPS(){
        regret = new int[N_MOVES];
        regret_sum = new int[N_MOVES];
        strategy = new double[N_MOVES];
        strategy_sum = new double[N_MOVES];
        
        for(int i = 0;i < strategy.length; i++){
            strategy[i] = 1.0/N_MOVES;
        }
    }
    
    int get_action(){
        double r = Math.random();
        
        if(r < strategy[0]){
            return 0;
        } else if(r < strategy[0] + strategy[1]){
            return 1;
        } else {
            return 2;
        }
    }
	
	int get_action(double s[]){
        double r = Math.random();
        
        if(r < s[0]){
            return 0;
        } else if(r < s[0] + s[1]){
            return 1;
        } else {
            return 2;
        }
    }
    
    void compute_regret(int action, int opp_action){
        for(int i = 0; i < N_MOVES; i++){
            regret[i] = utility(i, opp_action) - utility(action, opp_action);
        }
    }
    
    void update_regret_sum(){
        for(int i = 0; i < N_MOVES; i++){
            regret_sum[i] += regret[i];
        }
    }
    
    void get_strategy(){
        double normalizing_sum = 0;
        for(int i = 0; i < N_MOVES; i++){
            strategy[i] = regret_sum[i] > 0 ? regret_sum[i] : 0;
			normalizing_sum += strategy[i];
        }
        for(int i = 0; i < N_MOVES; i++){
			if(normalizing_sum > 0)
				strategy[i] /= normalizing_sum;
			else 
				strategy[i] = 1.0 / N_MOVES;
        }
    }
	
    
    void update_strategy_sum(){
        for(int i = 0; i < N_MOVES; i++){
            strategy_sum[i] += strategy[i];
        }
    }
    
    int utility(int choice, int opp_choice){
        if(choice == ROCK & opp_choice == PAPER){
            return -1;
        } else if(choice == ROCK & opp_choice == SCISSORS){
            return 1;
        } else if(choice == PAPER & opp_choice == ROCK){
            return 1;
        } else if(choice == PAPER & opp_choice == SCISSORS){
            return -1;
        } else if(choice == SCISSORS & opp_choice == ROCK){
            return -1;
        } else if(choice == SCISSORS & opp_choice == PAPER){
            return 1;
        } else {
            return 0;
        }
    }
    
    void normalize_strategy_sum(){
        double normalizing_sum = 0;
        for(int i = 0; i < N_MOVES; i++){
            normalizing_sum += strategy_sum[i];
        }
        for(int i = 0; i < N_MOVES; i++){
			if(normalizing_sum > 0)
				strategy_sum[i] /= normalizing_sum;
			else 
				strategy_sum[i] = 1.0 / N_MOVES;
        }
    }
	
	double game_value(double opp_strategy[]){
		double val = 0;
		for(int i = 0; i < N_MOVES; i++){
			for(int j = 0; j < N_MOVES; j++){
				val += (double)utility(i, j) * strategy_sum[i] * opp_strategy[j];
			}
		}
		return val;
	}
    
    void train(int n_iter){
		
		double[] opp_strategy = new double[] {0.2, 0.3, 0.5};
		
        for(int i = 0; i < n_iter; i++){
            int action = get_action();
            int opp_action = get_action(opp_strategy);
            
            compute_regret(action, opp_action);
            update_regret_sum();
            get_strategy();
            update_strategy_sum();
			
        }
        normalize_strategy_sum();
		
		System.out.println("EV: " + game_value(opp_strategy));
    }
    
    public String toString(){
        return "ROCK: " + strategy_sum[0] + " \n" +
               "PAPER: " + strategy_sum[1] + " \n" + 
               "SCISSORS: " + strategy_sum[2];
    }
    
    public static void main(String args[]){
        CFR_RPS rps = new CFR_RPS();
        rps.train(10_000_000);
		System.out.println(rps);
    }
}
