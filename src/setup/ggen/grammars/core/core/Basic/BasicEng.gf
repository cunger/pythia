--# -path=.:prelude:alltenses

instance BasicEng of Basic = open Prelude, SyntaxEng, ParadigmsEng in {

 oper

    makeAdv = overload {
    makeAdv : Prep -> NP  -> Adv        = \ prep,np -> SyntaxEng.mkAdv prep np;
    makeAdv : Subj -> S   -> Adv        = \ subj,s  -> SyntaxEng.mkAdv subj s;
    makeAdv : Conj -> Adv -> Adv -> Adv = \ c,a1,a2 -> SyntaxEng.mkAdv c a1 a2;
    };

    makeN2 : N -> N2 = \ n -> mkN2 n;

    make_empty_N2 : Prep -> N2 = \ prep -> mkN2 (mkN "" "") prep;

    NONE = overload {	
    NONE : A       = mkA  nonExist nonExist nonExist nonExist;
    NONE : A2      = mkA2 (mkA nonExist nonExist nonExist nonExist) noPrep;
    NONE : N       = mkN  nonExist nonExist nonExist nonExist;
    NONE : N2      = mkN2 (mkN nonExist nonExist nonExist nonExist);
    NONE : V       = mkV  nonExist nonExist nonExist nonExist nonExist;
    NONE : V2      = mkV2 (mkV nonExist nonExist nonExist nonExist nonExist);
    NONE : Prep    = noPrep;
    NONE : Adv     = ParadigmsEng.mkAdv nonExist;
    NONE : Cl      = mkCl (mkN nonExist nonExist nonExist nonExist);
    NONE : RCl     = mkRCl which_RP (mkV nonExist nonExist nonExist nonExist nonExist);
    NONE : CN      = mkCN (mkN nonExist nonExist nonExist nonExist);
    NONE : AP      = mkAP (mkA nonExist nonExist nonExist nonExist);
    NONE : NP      = mkNP (mkN nonExist nonExist nonExist nonExist);
    NONE : VP      = mkVP (mkV nonExist nonExist nonExist nonExist);
    NONE : SC      = mkSC (mkVP (mkV nonExist nonExist nonExist nonExist nonExist));
    NONE : S       = mkS  (mkCl (mkN nonExist nonExist nonExist nonExist));
    NONE : VPSlash = mkVPSlash (mkV2 (mkV nonExist nonExist nonExist nonExist));
    NONE : ClSlash = mkClSlash (mkNP (mkN nonExist nonExist nonExist nonExist)) (mkV2 (mkV nonExist nonExist nonExist nonExist));
    };

    DUMMY_A2   : A2   = mkA2 (mkA "XXX" "XXX" "XXX" "XXX") noPrep;
    DUMMY_N2   : N2   = mkN2 (mkN "XXX" "XXX" "XXX" "XXX");
    DUMMY_V2   : V2   = mkV2 (mkV "XXX" "XXX" "XXX" "XXX" "XXX");
    DUMMY_Prep : Prep = mkPrep "XXX";
    DUMMY_Adv  : Adv  = ParadigmsEng.mkAdv "XXX";
    DUMMY_CN   : CN   = mkCN (mkN "XXX" "XXX" "XXX" "XXX");
    DUMMY_AP   : AP   = mkAP (mkA "XXX" "XXX" "XXX" "XXX");
    DUMMY_NP   : NP   = mkNP (mkPN "XXX");
    DUMMY_VP   : VP   = mkVP (mkV "XXX" "XXX" "XXX" "XXX");

}