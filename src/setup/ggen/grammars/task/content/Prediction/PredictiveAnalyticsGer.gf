--# -path=.:prelude:alltenses

concrete PredictiveAnalyticsGer of PredictiveAnalytics = ClausesGer ** PredictiveAnalyticsI with (Basic = BasicGer), (Syntax = SyntaxGer) ** open ParadigmsGer in {

 oper

     expect_VS : VS  = mkVS  (mkV "erwarten");
     think_VS  : VS  = mkVS  (mkV "denken");

     probability_N2 : N2 = mkN2 (mkN "Wahrscheinlichkeit" "Wahrscheinlichkeiten" feminine);
     chance_N2      : N2 = mkN2 (mkN "Chance" "Chance" feminine);

}
