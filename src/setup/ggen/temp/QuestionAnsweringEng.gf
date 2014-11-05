--# -path=.:prelude:alltenses

concrete QuestionAnsweringEng of QuestionAnswering = ClausesEng ** QuestionAnsweringI with (Basic = BasicEng), (Syntax = SyntaxEng) ** open ParadigmsEng in {

   oper 

       give_V3 : V3 = mkV3 (mkV "give");
       show_V3 : V3 = mkV3 (mkV "show");
       get_V2  : V2 = mkV2 (mkV "get" "got" "gotten");

}
