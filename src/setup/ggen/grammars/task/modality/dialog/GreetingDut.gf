--# -path=.:prelude:alltenses

concrete GreetingDut of Greeting = open BasicDut, SyntaxDut in {

 lincat

    Greeting = Utt;

 lin

    Hello       = variants { mkDialogAct "hallo"; mkDialogAct "hi" };
    GoodDay     = mkDialogAct "goedemiddag";
    GoodEvening = mkDialogAct "goedenavond";
    GoodMorning = mkDialogAct "goedemorgen";
    GoodNight   = mkDialogAct "goedenacht";
    Goodbye     = mkDialogAct "tot ziens";
    Bye         = variants { mkDialogAct "dag"; mkDialogAct "doei" };
    SeeYou      = mkDialogAct "tot ziens";
  
    ReIntroduction    = mkDialogAct "leuk je te ontmoeten";
    ReIntroductionPol = mkDialogAct "leuk u te ontmoeten";
    HowAreYou         = mkDialogAct "hoe gaat het";

}
