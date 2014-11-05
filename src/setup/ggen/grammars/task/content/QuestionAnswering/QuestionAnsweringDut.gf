--# -path=.:prelude:alltenses

concrete QuestionAnsweringDut of QuestionAnswering = ClausesDut ** QuestionAnsweringI with (Basic = BasicDut), (Syntax = SyntaxDut) ** open ParadigmsDut in {

   oper 

       give_V3 : V3 = mkV3 (mkV "tonen");
       show_V3 : V3 = mkV3 (mkV "tonen");
       get_V2  : V2 = mkV2 (mkV "halen");

}
