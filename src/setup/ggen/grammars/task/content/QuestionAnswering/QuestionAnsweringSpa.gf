--# -path=.:prelude:alltenses

concrete QuestionAnsweringSpa of QuestionAnswering = ClausesSpa ** QuestionAnsweringI with (Basic = BasicSpa), (Syntax = SyntaxSpa) ** open ParadigmsSpa in {

   oper 

       give_V3 : V3 = mkV3 (mkV "dar");
       show_V3 : V3 = mkV3 (mkV "enseñar");
       show_V2 : V2 = mkV3 (mkV "enseñar");
       get_V2  : V2 = mkV2 (mkV "reqibir");
       list_V2 : V2 = mkV2 (mkV "listar");

}
