
abstract Clauses_ = Core ** {


 cat
  
    WhPron;
    WhAdv;

    Modality;

    Sentence_S;
    Question_QS;
    Imperative_Imp;

    Utterance_Utt;


 fun 

    ---- Modals ----

    MCan, MKnow, MMust, MWant : Modality;
    mod_clause : Modality -> Predicate -> Entity -> Statement;

    ---- Declarative sentences ----

    present_pos : Statement -> Sentence_S;
    present_neg : Statement -> Sentence_S;
    past_pos    : Statement -> Sentence_S;
    past_neg    : Statement -> Sentence_S;
    future_pos  : Statement -> Sentence_S;
    future_neg  : Statement -> Sentence_S;

    ---- Coordination ---- 
 
    AndSentence, OrSentence : Sentence_S -> Sentence_S -> Sentence_S;

    ---- Imperative ----

    imperative : Predicate -> Imperative_Imp;

    ---- Questions ----

    YesNo    : Statement -> Question_QS;
    queryAdv : WhAdv -> Statement -> Question_QS;

    query_subj   : Relation -> WhPron -> Entity -> Question_QS;
    query_obj    : Relation -> Entity -> WhPron -> Question_QS;

    query_pred   : WhPron -> Predicate -> Question_QS;

    query_copula_whPron : WhPron -> Entity -> Question_QS;
    query_copula_whAdv  : WhAdv  -> Entity -> Question_QS;

    ---- wh-forms ----
    
    Who  : WhPron;
    What : WhPron;
    Where, When, How, Why : WhAdv;

    WhichSg, WhichPl, HowMany : Predicate -> WhPron;

    ---- Utterances ----

    say     : Sentence_S     -> Utterance_Utt;
    ask     : Question_QS    -> Utterance_Utt;
    prompt  : Imperative_Imp -> Utterance_Utt;

}
