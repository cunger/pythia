--# -path=.:prelude:alltenses

concrete PredictiveAnalyticsDut of PredictiveAnalytics = ClausesDut ** PredictiveAnalyticsI with (Basic = BasicDut), (Syntax = SyntaxDut) ** open ParadigmsDut in {

 oper

     expect_VS : VS  = mkVS  (mkV "verwachten");
     think_VS  : VS  = mkVS  (mkV "denken");

     probability_N2 : N2 = mkN2 (mkN "waarschijnlijkheid" de);
     chance_N2      : N2 = mkN2 (mkN "kans" de);

}
