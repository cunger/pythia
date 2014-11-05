--# -path=.:prelude:alltenses

instance BasicDut of Basic = open Prelude, SyntaxDut, ParadigmsDut in {

 oper

    makeAdv = overload {
    makeAdv : Prep -> NP  -> Adv        = \ prep,np -> SyntaxDut.mkAdv prep np;
    makeAdv : Subj -> S   -> Adv        = \ subj,s  -> SyntaxDut.mkAdv subj s;
    makeAdv : Conj -> Adv -> Adv -> Adv = \ c,a1,a2 -> SyntaxDut.mkAdv c a1 a2;
    };

    makeN2 : N -> N2 = \ n -> mkN2 n;

    make_empty_N2 : Prep -> N2 = \ prep -> mkN2 (mkN "" "" het) prep;

    NONE = overload {	
    NONE : A       = mkA  nonExist nonExist nonExist nonExist nonExist;
    NONE : A2      = mkA2 (mkA nonExist nonExist nonExist nonExist nonExist) (mkPrep nonExist);
    NONE : N       = mkN  nonExist nonExist het;
    NONE : N2      = mkN2 (mkN nonExist nonExist het) (mkPrep nonExist);
    NONE : V       = mkV  nonExist nonExist nonExist nonExist nonExist nonExist;
    NONE : V2      = mkV2 (mkV nonExist nonExist nonExist nonExist nonExist nonExist);
    NONE : Prep    = mkPrep nonExist;
    NONE : Adv     = ParadigmsDut.mkAdv nonExist;
    NONE : Cl      = mkCl (mkN nonExist nonExist het);
    NONE : RCl     = mkRCl which_RP (mkV nonExist nonExist nonExist nonExist nonExist nonExist);
    NONE : CN      = mkCN (mkN nonExist nonExist het);
    NONE : AP      = mkAP (mkA nonExist nonExist nonExist nonExist nonExist);
    NONE : NP      = mkNP (mkN nonExist nonExist het);
    NONE : VP      = mkVP (mkV nonExist nonExist nonExist nonExist nonExist nonExist);
    NONE : SC      = mkSC (mkVP (mkV nonExist nonExist nonExist nonExist nonExist nonExist));
    NONE : S       = mkS  (mkCl (mkN nonExist nonExist het));
    NONE : VPSlash = mkVPSlash (mkV2 (mkV nonExist nonExist nonExist nonExist nonExist nonExist));
    NONE : ClSlash = mkClSlash (mkNP (mkN nonExist nonExist het)) (mkV2 (mkV nonExist nonExist nonExist nonExist nonExist nonExist));
    };

    DUMMY_A2   : A2   = mkA2 (invarA "XXX") (mkPrep "");
    DUMMY_N2   : N2   = variants { mkN2 (mkN "XXX" "XXX" het); mkN2 (mkN "XXX" "XXX" de) };
    DUMMY_V2   : V2   = mkV2 (mkV "XXX" "XXX" "XXX" "XXX" "XXX" "XXX" "XXX");
    DUMMY_Prep : Prep = mkPrep "XXX";
    DUMMY_Adv  : Adv  = ParadigmsDut.mkAdv "XXX";
    DUMMY_CN   : CN   = variants { mkCN (mkN "XXX" "XXX" het); mkCN (mkN "XXX" "XXX" de) };
    DUMMY_AP   : AP   = mkAP (invarA "XXX");
    DUMMY_NP   : NP   = mkNP (mkPN "XXX");
    DUMMY_VP   : VP   = mkVP (mkV "XXX" "XXX" "XXX" "XXX" "XXX" "XXX" "XXX");

}