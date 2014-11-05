--# -path=.:prelude:alltenses

incomplete concrete QuestionAnsweringI of QuestionAnswering = open Basic, Syntax in {

  lin

     GiveMeAll p = variants { mkImp (mkVP give_V3 i_NP (mkNP all_Predet (mkNP aPl_Det p.cn))); 
                              mkImp (mkVP give_V3 i_NP (mkNP every_Det p.cn));
                              mkImp (mkVP show_V3 i_NP (mkNP all_Predet (mkNP aPl_Det p.cn))); 
                              mkImp (mkVP show_V3 i_NP (mkNP every_Det p.cn)) };
     GetAll    p = variants { mkImp (mkVP get_V2 (mkNP all_Predet (mkNP aPl_Det p.cn)));
                              mkImp (mkVP get_V2 (mkNP every_Det p.cn)) };

}
