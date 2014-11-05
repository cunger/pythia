abstract Core_Anaphora = Core_ ** {

 cat

    Anaphor;

 fun

    I, We, YouSg, YouPl, He, She, It, They : Anaphor; 

    anaphor : Anaphor -> Entity;
    poss    : Anaphor -> Class -> Entity;

    this, that : Entity;

}
