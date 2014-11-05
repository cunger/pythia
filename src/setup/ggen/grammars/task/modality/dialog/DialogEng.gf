--# -path=.:prelude:alltenses

concrete DialogEng of Dialog = ClausesEng ** DialogI with (Basic=BasicEng), (Syntax=SyntaxEng) ** open ParadigmsEng in {


 oper

    give_V3 : V3 = mkV3 (mkV "give" "gave" "given");
    show_V3 : V3 = mkV3 (mkV "show" "showed" "shown");

    list_V2 : V2 = mkV2 (mkV "list");
    need_V2 : V2 = mkV2 (mkV "need");
    want_V2 : V2 = mkV2 (mkV "want");

}
