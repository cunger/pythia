--# -path=.:prelude:alltenses

concrete QuestionAnsweringGer of QuestionAnswering = ClausesGer ** QuestionAnsweringI with (Basic = BasicGer), (Syntax = SyntaxGer) ** open ParadigmsGer in {

   oper 

       give_V3 : V3 = mkV3 (mkV "geben");
       show_V3 : V3 = mkV3 (mkV "zeigen");
       get_V2  : V2 = mkV2 (mkV "holen");
}
