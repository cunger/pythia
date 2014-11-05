--# -path=.:prelude:alltenses

instance BasicGer of Basic = open Prelude, SyntaxGer, ParadigmsGer in {

 oper

    makeAdv = overload {
    makeAdv : Prep -> NP  -> Adv        = \ prep,np -> SyntaxGer.mkAdv prep np;
    makeAdv : Subj -> S   -> Adv        = \ subj,s  -> SyntaxGer.mkAdv subj s;
    makeAdv : Conj -> Adv -> Adv -> Adv = \ c,a1,a2 -> SyntaxGer.mkAdv c a1 a2;
    };

    makeN2 : N -> N2 = \ n -> mkN2 n;

    make_empty_N2 : Prep -> N2 = \ prep -> mkN2 (mkN "" "" "" "" "" "" neuter) prep;

    NONE = overload {
    NONE : A       = mkA  nonExist nonExist nonExist nonExist;
    NONE : A2      = mkA2 (mkA nonExist nonExist nonExist nonExist) accPrep;
    NONE : N       = mkN  nonExist nonExist nonExist nonExist nonExist nonExist neuter;
    NONE : N2      = mkN2 (mkN nonExist nonExist nonExist nonExist nonExist nonExist neuter);
    NONE : V       = mkV  nonExist nonExist nonExist nonExist nonExist nonExist;
    NONE : V2      = mkV2 (mkV nonExist nonExist nonExist nonExist nonExist nonExist);
    NONE : Prep    = accPrep;
    NONE : Adv     = ParadigmsGer.mkAdv nonExist;
    NONE : Cl      = mkCl (mkN nonExist nonExist nonExist nonExist nonExist nonExist neuter);
    NONE : RCl     = mkRCl which_RP (mkV nonExist nonExist nonExist nonExist nonExist nonExist);
    NONE : CN      = mkCN (mkN nonExist nonExist nonExist nonExist nonExist nonExist neuter);
    NONE : AP      = mkAP (mkA nonExist nonExist nonExist nonExist);
    NONE : NP      = mkNP (mkN nonExist nonExist nonExist nonExist nonExist nonExist neuter);
    NONE : VP      = mkVP (mkV nonExist nonExist nonExist nonExist nonExist nonExist);
    NONE : SC      = mkSC (mkVP (mkV nonExist nonExist nonExist nonExist nonExist nonExist));
    NONE : S       = mkS  (mkCl (mkN nonExist nonExist nonExist nonExist nonExist nonExist neuter));
    NONE : VPSlash = mkVPSlash (mkV2 (mkV nonExist nonExist nonExist nonExist nonExist nonExist));
    NONE : ClSlash = mkClSlash (mkNP (mkN nonExist nonExist nonExist nonExist nonExist nonExist neuter)) (mkV2 (mkV nonExist nonExist nonExist nonExist nonExist nonExist));
    };

    DUMMY_A2   : A2   = mkA2 (invarA "XXX") (mkPrep "");
    DUMMY_N2   : N2   = variants { mkN2 (mkN "XXX" "XXX" "XXX" "XXX" "XXX" "XXX" masculine); mkN2 (mkN "XXX" "XXX" "XXX" "XXX" "XXX" "XXX" feminine); mkN2 (mkN "XXX" "XXX" "XXX" "XXX" "XXX" "XXX" neuter) };
    DUMMY_V2   : V2   = mkV2 (mkV "XXX" "XXX" "XXX" "XXX" "XXX" "XXX");
    DUMMY_Prep : Prep = variants { mkPrep "XXX" accusative; mkPrep "XXX" dative; mkPrep "XXX" genitive };
    DUMMY_Adv  : Adv  = ParadigmsGer.mkAdv "XXX";
    DUMMY_CN   : CN   = variants { mkCN (mkN "XXX" "XXX" "XXX" "XXX" "XXX" "XXX" masculine); mkCN (mkN "XXX" "XXX" "XXX" "XXX" "XXX" "XXX" feminine); mkCN (mkN "XXX" "XXX" "XXX" "XXX" "XXX" "XXX" neuter) };
    DUMMY_AP   : AP   = mkAP (invarA "XXX");
    DUMMY_NP   : NP   = mkNP (mkPN "XXX");
    DUMMY_VP   : VP   = mkVP (mkV "XXX" "XXX" "XXX" "XXX" "XXX" "XXX");
    
}
