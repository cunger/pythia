
abstract DIT = Clauses ** {

 cat

    DialogAct;


    ---- General-Purpose Communicative Functions ----
  
    -- Information Seeking Functions
    Question;
    PropositionalQuestion;

    -- Information Providing Functions
    Inform;
    Answer;

    -- Action Discussion Functions

    ---- Dimension-Specific Communicative Functions ----
    ---- e.g. Open Meeting, Close Meeting, Bet, AcceptBet, Congratulation, Condolance, graphical/multimodal dialogue acts like Point, List, etc.


    ---- Dialogue Control Functions ----
    
    FeedbackFunctions;

    InteractionManagementFunctions;
    
    TurnManagement;
    TurnUnitInitialFunctions;
    TurnUnitFinalFunctions;
    TimeManagement;
    ContactManagement;
    DiscourseStructureManagement;

    SocialObligationsManagementFunctions;
    
    Salutation;
    SelfIntroduction;
    Apologizing; 
    GratitudeExpression;
    Valediction;


fun 

    question : Question_QS -> Question;
 -- setQuestion
 -- checkQuestion 
 -- posicheck 
 -- negacheck 
 -- choiceQuestion

    agreement    : Inform;
    disagreement : Inform;
    correction   : Inform; -- TODO Should probably be a function (corrected -> correct).

    confirm    : Answer;
    disconfirm : Answer;
    -- TODO What about a function for turning Proposition, Individual, Class into an Answer?

    initialGreeting         : Salutation;
    returnGreeting          : Salutation;
    initialSelfIntroduction : SelfIntroduction;
    returnSelfIntroduction  : SelfIntroduction;
    apology                 : Apologizing;
    apologyDownplay         : Apologizing;
    thanking                : GratitudeExpression;
    thankingDownplay        : GratitudeExpression;
    initialGoodbye          : Valediction;
    returnGoodbye           : Valediction;

    turnAccept  : TurnUnitInitialFunctions;
    turnGrab    : TurnUnitInitialFunctions;
    turnTake    : TurnUnitInitialFunctions;
    turnAssign  : TurnUnitFinalFunctions;
    turnKeep    : TurnUnitFinalFunctions;
    turnRelease : TurnUnitFinalFunctions;

    stalling               : TimeManagement;
    pausing                : TimeManagement; 

    contactCheck           : ContactManagement;
    contactIndication      : ContactManagement;

    opening                : DiscourseStructureManagement;
    preclosing             : DiscourseStructureManagement;
    topicIntroduction      : DiscourseStructureManagement;
    topicShiftAnnouncement : DiscourseStructureManagement;
    topicShift             : DiscourseStructureManagement;


    ---- Coercions 

    coerce_PropositionalQuestion_to_Question : PropositionalQuestion -> Question;

    coerce_Salutation_to_SocialObligationsManagementFunctions          : Salutation          -> SocialObligationsManagementFunctions;
    coerce_SelfIntroduction_to_SocialObligationsManagementFunctions    : SelfIntroduction    -> SocialObligationsManagementFunctions;
    coerce_Apologizing_to_SocialObligationsManagementFunctions         : Apologizing         -> SocialObligationsManagementFunctions;
    coerce_GratitudeExpression_to_SocialObligationsManagementFunctions : GratitudeExpression -> SocialObligationsManagementFunctions;
    coerce_Valediction_to_SocialObligationsManagementFunctions         : Valediction         -> SocialObligationsManagementFunctions;

    coerce_TurnUnitInitialFunctions_to_TurnManagement : TurnUnitInitialFunctions     -> TurnManagement;
    coerce_TurnUnitFinalFunctions_to_TurnManagement   : TurnUnitFinalFunctions       -> TurnManagement;

    coerce_TurnManagement_to_InteractionManagementFunctions              : TurnManagement               -> InteractionManagementFunctions;
    coerce_TimeManagement_to_InteractionManagementFunctions              : TimeManagement               -> InteractionManagementFunctions;
    coerce_ContactManagment_to_InteractionManagementFunctions            : ContactManagement            -> InteractionManagementFunctions;
    coerce_DiscourseStructureManagement_toInteractionManagementFunctions : DiscourseStructureManagement -> InteractionManagementFunctions;


    coerce_Question_to_DialogAct : Question -> DialogAct;
    coerce_Inform_to_DialogAct   : Inform   -> DialogAct;
    coerce_Answer_to_DialogAct   : Answer   -> DialogAct;

    coerce_SocialObligationsManagementFunctions_to_DialogAct : SocialObligationsManagementFunctions -> DialogAct;

    coerce_TurnManagement_to_DialogAct               : TurnManagement               -> DialogAct;
    coerce_TimeManagement_to_DialogAct               : TimeManagement               -> DialogAct;
    coerce_ContactManagement_to_DialogAct            : ContactManagement            -> DialogAct;
    coerce_DiscourseStructureManagement_to_DialogAct : DiscourseStructureManagement -> DialogAct;

}
