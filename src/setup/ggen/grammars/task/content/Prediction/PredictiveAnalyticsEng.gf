--# -path=.:prelude:alltenses

concrete PredictiveAnalyticsEng of PredictiveAnalytics = ClausesEng ** PredictiveAnalyticsI with (Basic = BasicEng), (Syntax = SyntaxEng) ** open ParadigmsEng in {

 oper

     expect_VS    : VS  = mkVS  (mkV "expect");
     think_VS     : VS  = mkVS  (mkV "think"); 

     probability_N2 : N2 = mkN2 (mkN "probability");
     chance_N2      : N2 = mkN2 (mkN "chance");

}
