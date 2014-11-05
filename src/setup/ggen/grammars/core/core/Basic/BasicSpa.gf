--# -path=.:prelude:alltenses

instance BasicSpa of Basic = open Prelude, SyntaxSpa, ParadigmsSpa in {

 oper

    makeAdv = overload {
    makeAdv : Prep -> NP  -> Adv        = \ prep,np -> SyntaxSpa.mkAdv prep np;
    makeAdv : Subj -> S   -> Adv        = \ subj,s  -> SyntaxSpa.mkAdv subj s;
    makeAdv : Conj -> Adv -> Adv -> Adv = \ c,a1,a2 -> SyntaxSpa.mkAdv c a1 a2;
    };

    makeN2 : N -> N2 = \ n -> deN2 n;

    make_empty_N2 : Prep -> N2 = \ prep -> mkN2 (mkN "" "" masculine) prep;

    NONE = overload {   
    NONE : A       = mkA  nonExist nonExist nonExist nonExist nonExist;
    NONE : A2      = mkA2 (mkA nonExist nonExist nonExist nonExist nonExist) (mkPrep "");
    NONE : N       = mkN  nonExist nonExist masculine;
    NONE : N2      = mkN2 (mkN nonExist nonExist masculine) (mkPrep "");
    NONE : V       = mkV  nonExist nonExist;
    NONE : V2      = mkV2 (mkV nonExist nonExist);
    NONE : Prep    = mkPrep "";
    NONE : Adv     = ParadigmsSpa.mkAdv nonExist;
    NONE : Cl      = mkCl (mkN nonExist nonExist masculine);
    NONE : RCl     = mkRCl which_RP (mkV nonExist nonExist);
    NONE : CN      = mkCN (mkN nonExist nonExist masculine);
    NONE : AP      = mkAP (mkA nonExist nonExist nonExist nonExist nonExist);
    NONE : NP      = mkNP (mkN nonExist nonExist masculine);
    NONE : VP      = mkVP (mkV nonExist nonExist);
    NONE : SC      = mkSC (mkVP (mkV nonExist nonExist));
    NONE : S       = mkS  (mkCl (mkN nonExist nonExist masculine));
    NONE : VPSlash = mkVPSlash (mkV2 (mkV nonExist nonExist));
    NONE : ClSlash = mkClSlash (mkNP (mkN nonExist nonExist masculine)) (mkV2 (mkV nonExist nonExist));
    };

    DUMMY_A2   : A2   = mkA2 (mkA "XXX" "XXX" "XXX" "XXX" "XXX") (mkPrep "");
    DUMMY_N2   : N2   = variants { mkN2 (mkN "XXX" "XXX" masculine) (mkPrep ""); mkN2 (mkN "XXX" "XXX" feminine) (mkPrep "") };
    DUMMY_V2   : V2   = mkV2 (mkV "XXX" "XXX");
    DUMMY_Prep : Prep = mkPrep "XXX";
    DUMMY_Adv  : Adv  = ParadigmsSpa.mkAdv "XXX";
    DUMMY_CN   : CN   = variants { mkCN (mkN "XXX" "XXX" masculine); mkCN (mkN "XXX" "XXX" feminine) };
    DUMMY_AP   : AP   = mkAP (mkA "XXX" "XXX" "XXX" "XXX" "XXX");
    DUMMY_NP   : NP   = mkNP (mkPN "XXX");
    DUMMY_VP   : VP   = mkVP (mkV "XXX" "XXX");

}