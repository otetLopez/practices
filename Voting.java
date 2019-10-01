import java.util.ArrayList;
import java.util.Random;

/*******************************************
 * Complete your program from here.
 *******************************************/
class Applicant {
    private String name = "";
    private int voters = 0;

    public Applicant(String name, int voters)  {
        this.name = name;
        this.voters = voters;
    }

    /** *
     * Developer              : C0768425
     * Applicant Class Method : copy constructor
     */
    Applicant(Applicant copy) {
        name = copy.name;
        voters = copy.voters;
    }
    /** *
     * Developer                : C0768425
     * Applicant Class Method   : elect
     * Purpose                  : Increments number of voters
     * Return                   : void
     */
    public void elect() {
        voters++;
    }

    /** *
     * Developer                : C0768425
     * Applicant Class Method   : init
     * Purpose                  : Resets number of voters
     * Return                   : void
     */
    public void init() {
        voters = 0;
    }

    /** *
     * Developer                : C0768425
     * Applicant Class Method   : getVotes
     * Purpose                  : Return number of voters
     * Return                   : int
     */
    public int getVotes() {
        return voters;
    }

    /** *
     * Developer                : C0768425
     * Applicant Class Method   : getName
     * Purpose                  : Return name of applicant
     * Return                   : String
     */
    public String getName() {
        return name;
    }
}

/**
 * Class Ballot
 * */
class Ballot {
    private ArrayList<Applicant> applicants;
    private ArrayList<Vote> votes;
    private int totalCount = 0;
    private int day = 0;

    public Ballot(ArrayList<Applicant> applicants, int totalVotersCount, int day) {
        /**
         * Each applicant shall be a copy of the one taken as argument - Pg.2 of Final document PDF
         * */
        this.applicants = new ArrayList<Applicant>();
        for(int i=0; i<applicants.size(); ++i) {
            this.applicants.add(new Applicant(applicants.get(i)));
        }
        totalCount = totalVotersCount;
        this.day = day;

        /** Default setting to resetVotersCount */
        resetVotersCount();

        /** Initialize Vote ArrayList */
        this.votes = new ArrayList<Vote>();
    }

    public Ballot(ArrayList<Applicant> applicants, int totalVotersCount, int day, boolean resetVote) {
        /**
         * each applicant shall be a copy of the one taken as argument - Pg.2 of Final document PDF
         * */
        this.applicants = new ArrayList<Applicant>();
        for(int i=0; i<applicants.size(); ++i) {
            this.applicants.add(new Applicant(applicants.get(i)));
        }
        //this.applicants = new ArrayList<Applicant>(applicants);

        totalCount = totalVotersCount;
        this.day = day;

        if(resetVote) {
            resetVotersCount();
        }
    }

    /** *
     * Developer                : C0768425
     * Ballot Class Method      : computeVoters
     * Purpose                  : Computes actual total number of voters
     * Return                   : int
     */
    public int computeVoters() {
        int total = 0;
        for(int i=0; i<applicants.size(); ++i) {
           total += applicants.get(i).getVotes() ;
        }
        return total;
    }

    /** *
     * Developer                : C0768425
     * Ballot Class Method      : winner
     * Purpose                  : Returns the name of the winning applicant
     *                          : In case of tie, it will return the latest search
     * Return                   : String
     */
    public String winner() {
        int voteNum = 0;
        int winNum = 0;
        String name = "";
        for(int i=0; i<applicants.size(); ++i) {
            voteNum = applicants.get(i).getVotes() ;
            if(voteNum >= winNum) {
                winNum = voteNum;
                name = applicants.get(i).getName();
            }
        }
        return name;
    }

    /** *
     * Developer                : C0768425
     * Ballot Class Method      : results
     * Purpose                  : Display results following format:
     *
     *                            Participation rate -> <participation rate of the vote> per cent
     *                            Effective number of voters -> <actual number of voters>
     *                            The chosen leader is -> <name of the elected leader>
     *                            Voter distribution
     *                            <name of applicant 1> -> <percent 1> percent of voters
     *                            .....
     *                            <name of applicant n> -> <percent n> percent of voters
     *
     * Return                   : void
     */
    public void results() {
        double total = computeVoters();
        double percent = 0;

        percent = (total/totalCount) * 100.0;

        System.out.printf("Participation rate -> %.1f percent\n", percent);
        System.out.println("Effective number of voters -> " + (int) total);
        System.out.println("The chosen chef is -> " + winner());

        if((int)total < 1)
            System.out.println("Canceled ballot, no voting");
        else {
            System.out.println("\nVoter distribution");
            for (int i = 0; i < applicants.size(); ++i) {
                System.out.print(applicants.get(i).getName() + " -> ");
                int appVotes = applicants.get(i).getVotes();
                //if(appVotes <= 0)
                //    System.out.println("Canceled ballot, no voting");
                //else
                System.out.printf("%.1f percent of voters\n", ((appVotes / total) * 100.0));
            }
            System.out.println("\n");
        }
    }

    /** *
     * Developer                : C0768425
     * Ballot Class Method      : simulate
     * Purpose                  :
     * Return                   : void
     */
    public void simulate(double pRate, int date) {
        /** Final Project PDF Pg. 5
         * compute the actual number of voters as the total number of voters (members
         * of the party) multiplied by the participation rate (and then, convert the result
         * to an int);
         * */
        int actualVotersNum = (int) (totalCount * pRate);
        /**
         * For each voter i, draw an integer at random candNum between 0 and the
         * number of applicants (minus 1) using the provided method Utils.randomInt;
         * */
        for(int i=0; i< actualVotersNum; ++i) {
            int candNum = Utils.randomInt(applicants.size());

            /**
             * If i%3 returns 0, add an electronic vote to votes in favor of the candidate with
             * index candNum in the list of applicants;
             *
             * If i%3 returns 1, add a paper ballot to votes in favor of the candidate with index
             * candNum in the list of applicants;
             *
             * If i%3 returns 2, add a vote sent in by mail votes in favor of the candidate with
             * index candNum in the list of applicants
             */

            /** all paper ballots from odd voters will be signed, and all others will not */
            boolean signed = true;
            if(i%2 == 0) {
                signed = false;
            }

            if((i%3) == 0) {
                Electronic newVote = new Electronic(applicants.get(candNum).getName(), date, day);
                this.votes.add(newVote);
                System.out.println(newVote);
            }
            else if((i%3) == 1) {
                PaperBallot newVote = new PaperBallot(applicants.get(candNum).getName(), date, day, signed);
                this.votes.add(newVote);
                System.out.println(newVote);
            }
            else {
                if((i%3) == 2) {
                    MailBallot newVote = new MailBallot(applicants.get(candNum).getName(), date, day, signed);
                    this.votes.add(newVote);
                    System.out.println(newVote);
                }
            }
        }
    }

    /** *
     * Developer                : C0768425
     * Ballot Class Method      : countVotes
     * Purpose                  : A method countVotes that updates the number of voters for each applicant
     *                          : given the contents of votes: for each valid ballot in votes, increment the
     *                          : number of voters of the applicant for which the vote was cast.
     * Return                   : void
     */
    public void countVotes() {
        for(int i=0; i<votes.size(); ++i) {
            if(votes.get(i).isInvalid() == false) { //so this is valid
                int idx = 0;
                while(!(votes.get(i).applicantName.matches(applicants.get(idx).getName()))) {
                    idx++;
                }
                applicants.get(idx).elect();
            }
        }
    }

    /** *
     * Developer                : C0768425
     * Ballot Class Method      : resetVotersCount
     * Purpose                  : Resets number of voters
     * Return                   : void
     */
    private void resetVotersCount() {
        for(int i=0; i<applicants.size(); ++i) applicants.get(i).init();
    }
}

/**
 * Class Vote
 */
class Vote {
    protected String applicantName;
    protected int actualDate = 0;
    protected int limitDate = 0;
    protected boolean validity = false;
    public Vote(String applicantName, int actualDate, int limitDate) {
        this.applicantName = applicantName;
        this.actualDate = actualDate;
        this.limitDate = limitDate;

    }

    /** *
     * Developer                : C0768425
     * Vote Class Method        : isInvalid
     * Purpose                  : ???
     * Return                   : boolean
     */
    public boolean isInvalid() {
        return validity;
    }

    /** *
     * Developer                : C0768425
     * Vote Class Method        : getDate
     * Purpose                  : returns date
     * Return                   : int
     */
    public int getDate() {
        return actualDate;
    }

    /** *
     * Developer                : C0768425
     * Vote Class Method        : getLimitDate
     * Purpose                  : returns limit date
     * Return                   : int
     */
    public int getLimitDate() {
        return limitDate;
    }

    /** *
     * Developer                : C0768425
     * Vote Class Method        : toString
     * Purpose                  : Displays and redefines toString method that produces a String
     *                          : representation of the ballot strictly respecting the following format:
     *                          : for <name of the applicant> -> invalid if the ballot is invalid and
     *                          : for <name of the applicant> -> valid if it is valid where <name of the applicant>
     *                          : is the name of the applicant for which this vote has been cast. Please note the leading
     *                          : space character in the above representations.
     * Return                   : void
     */
    public String toString() {
        return String.format(applicantName + " -> " + (this.isInvalid()==true ? "invalid" : "valid"));
    }
}
/**
 *  Class Electronic extends Vote
 * */
class Electronic extends Vote implements CheckBallot {
    public Electronic(String applicantName, int actualDate, int limitDate) {
        super(applicantName, actualDate, limitDate);
        toString();
    }
    /**
     * Developer                : C0768425
     * Electronic Class Method  : isInvalid
     * Purpose                  : Final Project PDF Pg. 3
     *                          : An electronic vote is invalid if its cast date is strictly superior
     *                          : to the limit date minus two
     *                          : (electronic votes must be sent in earlier than the others)
     * Return                   : boolean (true if invalid; false otherwise)
     */
    public boolean isInvalid() {
        // checkDate returns true for valid ballots, false otherwise
        // isInvalid returns true for invalid ballots, false otherwise
        return (checkDate() ? false : true);
    }
    @Override
    public boolean checkDate() {
        return (getDate() > (getLimitDate()-2) ? false : true);
    }

    public String toString() {
        return("electronic vote for " + applicantName + " -> " + (this.isInvalid()==true ? "invalid" : "valid"));
    }
}
/**
 * PaperBallot Class extends Vote
 * */
class PaperBallot extends Vote {
    protected boolean signed = true;
    public PaperBallot(String applicantName, int actualDate, int limitDate) {
        super(applicantName, actualDate, limitDate);
        toString();
    }
    public PaperBallot(String applicantName, int actualDate, int limitDate, boolean signed) {
        super(applicantName, actualDate, limitDate);
        this.signed = signed;
        toString();
    }
    public String toString() {
        return("vote by paper ballot for " + applicantName + " -> " + (this.isInvalid()==true ? "invalid" : "valid"));
    }
    /**
     * Developer                    : C0768425
     * PaperBallot Class Method     : isInvalid
     * Purpose                      : Final Project PDF Pg. 3
     *                              : A paper ballot is invalid if the ballot hasn’t been signed:
     *                              : a Boolean attribute will indicate this being or not the case.
     *                              : This attribute shall be initialized by a constructor taking a Boolean
     *                              : value as the last argument (true means that it is signed)
     * Return                       : boolean
     */
    public boolean isInvalid() {
        // If signed, ballot is valid, return false for isInvalid().  Otherwise return true(invalid).
        return ((signed==true) ? false : true);
    }
}

/**
 * MailBallot Class extends PaperBallot
 */
class MailBallot extends PaperBallot implements CheckBallot {
    private boolean valid = true;
    public MailBallot(String applicantName, int actualDate, int limitDate) {
        super(applicantName, actualDate, limitDate);
        toString();
    }
    public MailBallot(String applicantName, int actualDate, int limitDate, boolean signed) {
        super(applicantName, actualDate, limitDate, signed);
        toString();
    }
    /**
     * Developer                    : C0768425
     * MailBallot Class Method      : isInvalid
     * Purpose                      : Final Project PDF Pg. 3Final Project PDF Pg. 3
     *                              : A vote sent in by mail is invalid if it hasn’t been signed or
     *                              : if its cast date is strictly superior to the limit date.
     * Return                       : boolean
     */
    public boolean isInvalid() {
        boolean invalid = false; // Mail is valid by default
        // If signed, ballot is valid, return false for isInvalid().  Otherwise return true(invalid).
        if(signed==false || !checkDate())
            invalid = true;
        return invalid;
    }

    @Override
    public boolean checkDate() {
        return (getDate()>getLimitDate() ? false : true);
    }
    public String toString() {
        return("mailing of vote by paper ballot for " + applicantName + " -> " + (this.isInvalid()==true ? "invalid" : "valid"));
    }
}

/**
 * Developer                    : C0768425
 * Interface                    : CheckBallot
 * Purpose                      : Final Project PDF Pg. 4
 *                              : The classes that require a checking dates to determine validity
 *                              : shall implement an interface CheckBallot that requires the implementation
 *                              : of a method Boolean checkDate(). This method should be used whenever it
 *                              : is necessary to check dates to determine validity of a ballot.
 *                              : This method will return true when the date is valid.
 * Return                       : boolean
*/
interface CheckBallot {
    boolean checkDate();
}
/*******************************************
 * Do not modify the next part of code
 *******************************************/

class Utils {

    private static final Random RANDOM = new Random();

    // DO NOT USE THIS METHOD INSIDE YOUR CODE
    public static void setSeed(long seed) {
        RANDOM.setSeed(seed);
    }

    // generate a whole number between 0 and max (max not included)
    public static int randomInt(int max) {
        return RANDOM.nextInt(max);
    }
}

/**
 * Class for testing the simulation
 */

class Voting {

    public static void main(String args[]) {
        Utils.setSeed(20000);
        // TEST 1
        System.out.println("Test part I:");
        System.out.println("--------------");

        ArrayList<Applicant> applicants = new ArrayList<Applicant>();
        applicants.add(new Applicant("Tarek Oxlama", 2));
        applicants.add(new Applicant("Nicolai Tarcozi", 3));
        applicants.add(new Applicant("Vlad Imirboutine", 2));
        applicants.add(new Applicant("Angel Anerckjel", 4));

        // 30 -> nombre maximal of voters
        // 15 day of poll 
        Ballot ballot = new Ballot(applicants, 30, 15, false);

        ballot.results();

        // END OF TEST 1

        // TEST 2
        System.out.println("Test part II:");
        System.out.println("---------------");

        ballot = new Ballot(applicants, 20, 15);
        /*
        // all the ballots pass the check of the date
        // the parameters to simulate are in order:
        // the percentage of voters and the day of the vote
         */
        ballot.simulate(0.75, 12);
        ballot.countVotes();
        ballot.results();

        ballot = new Ballot(applicants, 20, 15);

        ballot.simulate(0.75, 15);
        ballot.countVotes();
        ballot.results();

        ballot = new Ballot(applicants, 20, 15);

        ballot.simulate(0.75, 15);
        ballot.countVotes();
        ballot.results();
        //END OF TEST 2
    }
}
