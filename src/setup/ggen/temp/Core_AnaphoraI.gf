--# -path=.:prelude:alltenses

incomplete concrete Core_AnaphoraI of Core_Anaphora = Core_I ** open Basic, Syntax in {

 lincat 

    Anaphor = Pron;


 lin

    I     = i_Pron;
    We    = we_Pron;
    YouSg = youSg_Pron;
    YouPl = youPl_Pron;
    He    = he_Pron;
    She   = she_Pron;
    It    = it_Pron;
    They  = they_Pron; 

    anaphor a   = mkEntity (mkNP a);
    poss    a c = mkEntity (mkNP a c.cn);

    this = mkEntity this_NP;
    that = mkEntity that_NP;

};
