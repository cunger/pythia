--# -path=.:prelude:alltenses

incomplete concrete Core_DatatypesI of Core_Datatypes = Core_I ** open Symbol, Basic, Syntax in {

 lincat

    Unit    = { s : Str };
    Measure = { a : A; ada : AdA };

 lin

    value_int   n = mkEntity (mkNP_int   n);
    value_float n = mkEntity (mkNP_float n);

    value_int_unit   n u = mkEntity (mkNP_int_unit   n u);
    value_float_unit n u = mkEntity (mkNP_float_unit n u);

    range_int   n1 n2 = mkEntity (mkNP (mkCN (make_empty_N2 between_Prep) (mkNP and_Conj (mkNP_int   n1) (mkNP_int   n2))));
    range_float n1 n2 = mkEntity (mkNP (mkCN (make_empty_N2 between_Prep) (mkNP and_Conj (mkNP_float n1) (mkNP_float n2))));


    a_few   u = mkEntity (unitDet a_few_Str   u);
    several u = mkEntity (unitDet several_Str u);

    at_least_int n = mkEntity (numDet_int at_least_Str n);
    at_most_int  n = mkEntity (numDet_int at_most_Str  n);
        
    at_least_float n = mkEntity (numDet_float at_least_Str n);
    at_most_float  n = mkEntity (numDet_float at_most_Str  n);

    at_least_int_unit n u = mkEntity (numunitDet_int at_least_Str n u);
    at_most_int_unit  n u = mkEntity (numunitDet_int at_most_Str  n u);
        
    at_least_float_unit n u = mkEntity (numunitDet_float at_least_Str n u);
    at_most_float_unit  n u = mkEntity (numunitDet_float at_most_Str  n u);

    
    -- descriptive p m = mkPredicate (mkAP m.ada p.ap);

    -- Low    = { a = slight_A; ada = slightly_AdA };
    -- Medium = { a = medium_A; ada = moderately_AdA };
    -- High   = { a = high_A;   ada = significantly_AdA };


 oper 

    -- number (+ unit) -> NP 

    mkNP_int   : { s : Str } -> NP = \ n -> mkNP (IntPN   n);
    mkNP_float : { s : Str } -> NP = \ n -> mkNP (FloatPN n);

    mkNP_int_unit   : { s : Str } -> { s : Str } -> NP = \ n, u -> mkNP (IntPN   { s = n.s ++ u.s });
    mkNP_float_unit : { s : Str } -> { s : Str } -> NP = \ n, u -> mkNP (FloatPN { s = n.s ++ u.s });

    -- number (+ unit) -> AdA

    mkAdA_int   : { s : Str } -> AdA = \ n -> mkAdA n.s;
    mkAdA_float : { s : Str } -> AdA = \ n -> mkAdA n.s;

    mkAdA_int_unit   : { s : Str } -> { s : Str } -> AdA = \ n, u -> mkAdA (n.s ++ u.s);
    mkAdA_float_unit : { s : Str } -> { s : Str } -> AdA = \ n, u -> mkAdA (n.s ++ u.s);

    -- 

    unitDet : Str -> { s : Str } -> NP = \ s,u -> mkNP (IntPN { s = s ++ u.s });

    numDet_int   : Str -> { s : Str } -> NP = \ s,n -> mkNP (IntPN   { s = s ++ n.s });
    numDet_float : Str -> { s : Str } -> NP = \ s,n -> mkNP (FloatPN { s = s ++ n.s });

    numunitDet_int   : Str -> { s : Str } -> { s : Str } -> NP = \ s,n,u -> mkNP (IntPN   { s = s ++ n.s ++ u.s });
    numunitDet_float : Str -> { s : Str } -> { s : Str } -> NP = \ s,n,u -> mkNP (FloatPN { s = s ++ n.s ++ u.s });

};
