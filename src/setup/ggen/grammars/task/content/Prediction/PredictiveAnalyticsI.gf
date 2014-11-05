--# -path=.:prelude:alltenses

incomplete concrete PredictiveAnalyticsI of PredictiveAnalytics = open Basic, Syntax in {

 param 

    Potentiality = Certain | Uncertain ;

 lincat 

    Prediction = Potentiality => S;
    Attitude   = { vs : VS; potentiality : Potentiality };

 lin

    predict     s   = table { Uncertain => mkS conditionalTense s;
                              Certain   => mkS futureTense s };

    predict_not s   = table { Uncertain => mkS conditionalTense negativePol s;
                              Certain   => mkS futureTense negativePol s };

    probability_exact p e = mkS (mkCl (mkCN (mkCN chance_N2 e.np) (p ! Certain)));
                         -- mkS (makeAdv with_Prep (mkNP aSg_Det (mkCN probability_N2 e.np))) (p ! Certain);
    probability_descr p m = mkS (mkCl (mkCN (mkCN m.a (mkCN chance_N2)) (p ! Certain)));

    confidence        p a = mkS (mkCl i_NP a.vs (p ! a.potentiality));

    Think  = { vs = think_VS;  potentiality = Certain };
    Expect = { vs = expect_VS; potentiality = Certain };

    plain p = p ! Certain;

}
