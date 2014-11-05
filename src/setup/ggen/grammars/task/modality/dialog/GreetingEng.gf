--# -path=.:prelude:alltenses

concrete GreetingEng of Greeting = open BasicEng, SyntaxEng in {

 lincat

    Greeting = Utt;

 lin

    Hello       = variants { mkDialogAct "hello"; mkDialogAct "hi" };
    GoodDay     = mkDialogAct "good day";
    GoodEvening = mkDialogAct "good evening";
    GoodMorning = mkDialogAct "good morning";
    GoodNight   = mkDialogAct "good night";
    Goodbye     = mkDialogAct "goodbye";
    Bye         = mkDialogAct "bye";
    SeeYou      = mkDialogAct "see you";
  
    ReIntroduction, ReIntroductionPol = mkDialogAct "nice to meet you";
    HowAreYou   = mkDialogAct "how are you";

}
