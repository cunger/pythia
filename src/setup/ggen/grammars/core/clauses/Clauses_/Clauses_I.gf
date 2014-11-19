--# -path=.:prelude:alltenses

incomplete concrete Clauses_I of Clauses_ = Core_I ** open Basic, Syntax in {


 lincat

    Modality = VV;

    Sentence_S     = S;
    Question_QS    = QS;
    Imperative_Imp = Imp;
    Phrase_Phr     = Phr;

    WhPron = IP;
    WhAdv  = IAdv; 


 lin 

    ---- Modals ----

    MWant = want_VV;
    MCan  = can_VV;
    MKnow = can8know_VV;
    MMust = must_VV;

    mod_clause m p e = mkCl e.np m p.vp;

    ---- Declarative sentences ----

    present_pos cl = mkS presentTense positivePol cl;
    present_neg cl = mkS presentTense negPol cl;
    past_pos    cl = mkS pastTense    positivePol cl;
    past_neg    cl = mkS pastTense    negPol cl;
    future_pos  cl = mkS futureTense  positivePol cl;
    future_neg  cl = mkS futureTense  negPol cl;

    ---- Coordination ----

    AndSentence  s1 s2 = mkS and_Conj s1 s2;
    OrSentence   s1 s2 = mkS or_Conj  s1 s2;

    ---- Imperative ----

    imperative p = mkImp p.vp;

    ---- Questions ---- 

    YesNo      s = mkQS s;
    queryAdv a s = mkQS (mkQCl a s);

    query_subj r wh e = case r.reverse of {
                             True  => mkQS (mkQCl wh (mkClSlash e.np (mkVPSlash r.v2)));
                             False => mkQS (mkQCl wh (makeVP r e))
                            };

    query_obj  r e wh = case r.reverse of {
                             True  => mkQS (mkQCl wh (makeVP r e)); 
                             False => mkQS (mkQCl wh (mkClSlash e.np (mkVPSlash r.v2)))
                           };

    query_pred          wh p = mkQS (mkQCl wh (makeVP p)); 
    query_copula_whPron wh e = mkQS (mkQCl (mkIComp wh) e.np); 
    query_copula_whAdv  wh e = mkQS (mkQCl (mkIComp wh) e.np);

    ---- wh-forms ----

    Who   = who_IP;
    What  = what_IP;
    Where = where_IAdv;
    When  = when_IAdv;
    How   = how_IAdv;
    Why   = why_IAdv;

    WhichSg p = mkIP which_IDet    p.cn; 
    WhichPl p = mkIP whichPl_IDet  p.cn;
    HowMany p = mkIP how8many_IDet p.cn;


    ---- Phrases ----

    say     s = mkPhr s;
    ask     q = mkPhr q;
    prompt  i = mkPhr i;

}
