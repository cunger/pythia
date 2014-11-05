--# -path=.:prelude:alltenses

concrete DialogDut of Dialog = ClausesDut ** DialogI with (Basic=BasicDut), (Syntax=SyntaxDut) ** open ParadigmsDut in {


 oper

    give_V3 : V3 = mkV3 (mkV "geven");
    show_V3 : V3 = mkV3 (mkV "tonen");

    list_V2 : V2 = mkV2 (mkV "noteren");
    need_V2 : V2 = mkV2 (mkV "nodig hebben"); -- TODO
    want_V2 : V2 = mkV2 (mkV "willen"); 
 -- want_VV is already defined in StructuralDut

 }
