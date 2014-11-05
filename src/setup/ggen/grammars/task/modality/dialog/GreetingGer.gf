--# -path=.:prelude:alltenses

concrete GreetingGer of Greeting = open BasicGer, SyntaxGer in {

 lincat

    Greeting = Utt;

 lin

    Hello       = variants { mkDialogAct "hallo"; mkDialogAct "hi" };
    GoodDay     = variants { mkDialogAct "guten tag"; mkDialogAct "tag" };
    GoodEvening = variants { mkDialogAct "guten abend"; mkDialogAct "nabend" };
    GoodMorning = variants { mkDialogAct "guten morgen"; mkDialogAct "morgen"; mkDialogAct "moin" };
    GoodNight   = variants { mkDialogAct "gute nacht"; mkDialogAct "nacht" };
    Goodbye     = variants { mkDialogAct "auf wiedersehen"; mkDialogAct "wiedersehen" };
    Bye         = variants { mkDialogAct "tschuess"; mkDialogAct "ciao" };
    SeeYou      = mkDialogAct "bis bald";
  
    ReIntroduction, ReIntroductionPol = mkDialogAct "sehr erfreut";
    HowAreYou   = mkDialogAct "wie geht's";

}
