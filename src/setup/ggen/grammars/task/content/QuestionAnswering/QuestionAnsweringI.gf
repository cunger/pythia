--# -path=.:prelude:alltenses

incomplete concrete QuestionAnsweringI of QuestionAnswering = open Basic, Syntax in {

  lin

     Get p = variants { -- give me 
                        mkImp (mkVP give_V3 i_NP (mkNP all_Predet (mkNP aPl_Det p.cn))); 
                        mkImp (mkVP give_V3 i_NP (mkNP every_Det p.cn));
                        mkImp (mkVP give_V3 i_NP (mkNP theSg_Det p.cn));
                        mkImp (mkVP give_V3 i_NP (mkNP thePl_Det p.cn));
                        -- show me 
                        mkImp (mkVP show_V3 i_NP (mkNP all_Predet (mkNP aPl_Det p.cn))); 
                        mkImp (mkVP show_V3 i_NP (mkNP every_Det p.cn)); 
                        mkImp (mkVP show_V3 i_NP (mkNP theSg_Det p.cn));
                        mkImp (mkVP show_V3 i_NP (mkNP thePl_Det p.cn));
                        -- show 
                        mkImp (mkVP show_V2 (mkNP all_Predet (mkNP aPl_Det p.cn)));
                        mkImp (mkVP show_V2 (mkNP theSg_Det p.cn));
                        mkImp (mkVP show_V2 (mkNP thePl_Det p.cn));
                        -- get  
                        mkImp (mkVP get_V2 (mkNP all_Predet (mkNP aPl_Det p.cn)));
                        mkImp (mkVP get_V2 (mkNP every_Det p.cn));
                        mkImp (mkVP get_V2 (mkNP theSg_Det p.cn));
                        mkImp (mkVP get_V2 (mkNP thePl_Det p.cn));
                        -- list 
                        mkImp (mkVP list_V2 (mkNP all_Predet (mkNP aPl_Det p.cn)));
                        mkImp (mkVP list_V2 (mkNP theSg_Det p.cn));
                        mkImp (mkVP list_V2 (mkNP thePl_Det p.cn));
                      };

     WhatIsThe p = mkQS (mkQCl what_IP (mkNP the_Det p.cn)); 
     WhoIsThe  p = mkQS (mkQCl who_IP  (mkNP the_Det p.cn));

}
