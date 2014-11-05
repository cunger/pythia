
abstract Dialog = Clauses ** {

 cat

    DialogAct;

fun 
    
    ---- Dialog acts ----

    Request : Utterance_Utt -> DialogAct;
    Provide : Utterance_Utt -> DialogAct;

    Open, Close, Ack, Thank  : DialogAct;


    -- Customer service

    --Need    : DialogPartner -> (c : Class) -> Entity c -> Clause_Cl;
    --Want    : DialogPartner -> (c : Class) -> Entity c -> Clause_Cl;
    --WantTo  : DialogPartner -> Statement -> Clause_Cl;

    DoYouHave_Class      : (c : Class) -> Predicate c -> Utterance_Utt;
    DoYouHave_Individual : (c : Class) -> Entity    c -> Utterance_Utt;

    -- How about ...? 
    -- I would like to know the ... you have ...
    -- What does ... mean? What does ... stand for?

}
