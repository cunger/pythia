--# -path=.:prelude:alltenses

concrete DialogGer of Dialog = ClausesGer ** DialogI with (Basic=BasicGer), (Syntax=SyntaxGer) ** open ParadigmsGer in {


 oper

    give_V3 : V3 = mkV3 (mkV "geben" "gibt" "gib" "gab" "gaebe" "gegeben") datPrep accPrep;
    show_V3 : V3 = mkV3 (mkV "zeigen") datPrep accPrep;

    list_V2 : V2 = mkV2 (mkV "auflisten");
    need_V2 : V2 = mkV2 (mkV "brauchen");
    want_V2 : V2 = mkV2 (mkV "wollen");

 }
