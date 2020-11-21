import java.util.*;



public class Main
{


    //System.setOut(new PrintStream(new FileOutputStream("output.txt")));

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] strings = input.split("#");
        if(strings.length !=2 || strings[0].charAt(0) !='1' || strings[1].charAt(0) !='1'){
            System.out.println("Invalid input");
            return ;
        }



        TuringMachine TM1 = MachinesLibrary.EqualBinaryWords();

        boolean done = TM1.Run(input, false);
        if (done==true)
        {
            System.out.println("Yes");
        }
        else
        {
            System.out.println("No");
        }
    }

}





class TuringMachine
{
    private Set<String> StateSpace;
    private ArrayList<Transition> TransitionSpace;
    private String StartState;
    private String AcceptState;
    private String RejectState;

    private String inputTape;
    private String outputTape;
    private String CurrentState;
    private int CurrentSymbol;
    private int Currentoutput;


    class Transition
    {
        String readState;
        char readSymbol;
        char readMEM;
        String writeState;
        char writeSymbol;
        int moveDirectioninput;
        int moveDirectionoutput;



        boolean isConflicting(String state, char symbol)
        {
            return false;
        }
    }


    public TuringMachine()
    {
        StateSpace = new HashSet<String>();
        TransitionSpace = new ArrayList<Transition>();
        StartState = new String("");
        AcceptState = new String("");
        RejectState = new String("");
        inputTape = new String("");
        outputTape = new String("Z");
        CurrentState = new String("");
        CurrentSymbol = 0;
        Currentoutput = 0;

    }

    public boolean Run(String input, boolean silentmode)
    {
        CurrentState = StartState;
        inputTape = input;

        while(!CurrentState.equals(AcceptState) && !CurrentState.equals(RejectState))
        {
            boolean foundTransition = false;
            Transition CurrentTransition = null;

            if (silentmode == false)
            {
                System.out.println(CurrentState + ", " +inputTape.substring(0, CurrentSymbol) + "^" +  inputTape.substring(CurrentSymbol) + ", "+outputTape.substring(0, Currentoutput) + "^" +  outputTape.substring(Currentoutput) );


            }



            Iterator<Transition> TransitionsIterator = TransitionSpace.iterator();
            while ( TransitionsIterator.hasNext() && foundTransition == false)
            {
                Transition nextTransition = TransitionsIterator.next();
               /* System.out.println("nextread: " + nextTransition.readSymbol + " charAtinput" + inputTape.charAt(CurrentSymbol) +
                        " transitionMem: " + nextTransition.readMEM + Currentoutput);*/
                if (nextTransition.readState.equals(CurrentState) && nextTransition.readSymbol == inputTape.charAt(CurrentSymbol) &&
                        (nextTransition.readMEM == '_' || Currentoutput<outputTape.length()&& nextTransition.readMEM == outputTape.charAt(Currentoutput) )  )

                //newTM.addTransition("q0", '1', 'Z', "q0", 'Z', 0, 1);

                {
                    foundTransition = true;
                    CurrentTransition = nextTransition;
                }
            }

            if (foundTransition == false)
            {
                return false;
            }
            else
            {
                if (outputTape.length() == Currentoutput)
                {
                    outputTape = outputTape.concat("_");
                }
                CurrentState = CurrentTransition.writeState;
                char[] tempTape = outputTape.toCharArray();
                tempTape[Currentoutput] = CurrentTransition.writeSymbol;
                outputTape =  new String(tempTape);
                CurrentSymbol += CurrentTransition.moveDirectioninput;
                Currentoutput += CurrentTransition.moveDirectionoutput;

                if (CurrentSymbol < 0)
                    CurrentSymbol = 0;

            }

        }

        if (CurrentState.equals(AcceptState))
        {
            return true;
        }
        else
        {
            return false;
        }


    }

    public boolean addState(String newState)
    {
        if (StateSpace.contains(newState))
        {
            return false;
        }
        else
        {
            StateSpace.add(newState);
            return true;
        }
    }

    public boolean setStartState(String newStartState)
    {
        if (StateSpace.contains(newStartState))
        {
            StartState = newStartState;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean setAcceptState(String newAcceptState)
    {
        if (StateSpace.contains(newAcceptState) && !RejectState.equals(newAcceptState))
        {
            AcceptState = newAcceptState;
            return true;
        }
        else
        {
            return false;
        }

    }

    public boolean setRejectState(String newRejectState)
    {
        if (StateSpace.contains(newRejectState) && !AcceptState.equals(newRejectState))
        {
            RejectState = newRejectState;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean addTransition(String rState, char rSymbol,char memSymbol ,String wState, char wSymbol, int inputDirection,int outputDirection)
    {
        if(!StateSpace.contains(rState) || !StateSpace.contains(wState))
        {
            return false;
        }

        boolean conflict = false;
        Iterator<Transition> TransitionsIterator = TransitionSpace.iterator();
        while ( TransitionsIterator.hasNext() && conflict == false)
        {
            Transition nextTransition = TransitionsIterator.next();
            if (nextTransition.isConflicting(rState, rSymbol))
            {
                conflict = true;
            }

        }
        /*if (conflict == true)
        {
            return false;
        }
        else
        {*/
            Transition newTransition = new Transition();
            newTransition.readState = rState;
            newTransition.readSymbol = rSymbol;
            newTransition.writeState = wState;
            newTransition.writeSymbol = wSymbol;
            newTransition.moveDirectioninput = inputDirection;
            newTransition.moveDirectionoutput = outputDirection;
            newTransition.readMEM = memSymbol;
            TransitionSpace.add(newTransition);
            return true;

    }
}




final class MachinesLibrary
{
    private MachinesLibrary() {}

    public static TuringMachine EqualBinaryWords()
    {
        TuringMachine newTM = new TuringMachine();
        newTM.addState("q0");
        newTM.addState("q1");
        newTM.addState("q2");
        newTM.addState("q3");
        newTM.addState("q4");

        newTM.setStartState("q0");
        newTM.setAcceptState("q4");
        //newTM.setRejectState("qr");

        //stay in q0
        newTM.addTransition("q0", '0', 'Z', "q0", 'Z', 0, 1);
        newTM.addTransition("q0", '1', 'Z', "q0", 'Z', 0, 1);
        newTM.addTransition("q0", '0', '_', "q0", '0', 1, 1);
        newTM.addTransition("q0", '1', '_', "q0", '1', 1, 1);
        //move to q1
        newTM.addTransition("q0", '#', '_', "q1", '_', 1, -1);
        //stay in q1
        newTM.addTransition("q1", '1', '1', "q1", '1', 1, -1);
        newTM.addTransition("q1", '0', '0', "q1", '0', 1, -1);
        newTM.addTransition("q1", '1', '0', "q1", '0', 1, -1);
        newTM.addTransition("q1", '0', '1', "q1", '1', 1, -1);
        //move to q2
        newTM.addTransition("q1", '_', 'Z', "q2", 'Z', -1, 0);
        //move to q4 from q1
        newTM.addTransition("q1", '_', '1', "q4", '1', 0, 0);
        newTM.addTransition("q2", '_', '0', "q4", '0', 0, 0);
        //stay in q2
        newTM.addTransition("q2", '0', 'Z', "q2", 'Z', -1, 0);
        newTM.addTransition("q2", '1', 'Z', "q2", 'Z', -1, 0);
        //move to q3
        newTM.addTransition("q2", '#', 'Z', "q2", 'Z', 1, 1);
        //stay in q3
        newTM.addTransition("q3", '1', '1', "q3", '1', 1, 1);
        newTM.addTransition("q3", '0', '0', "q3", '0', 1, 1);
        //move to q4 from q3
        newTM.addTransition("q3", '0', '1', "q4", '1', 0, 0);



        return newTM;
    }

}

