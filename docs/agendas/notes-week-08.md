Group 61 Team Meeting Week 8 Notes

Announcements by the team: We finished quite a lot of stuff! (adding expenses to event, choosing server, UI progress, statistics)
Questions for Mike:
Q: "Does undo need to undo multiple times?"; A: - yes, and quite a lot of times actually (for changes to expenses), 
but only in the current event
Q: "Does it undo it from the DB or visually?"; A: - visually, as it should usually be done before reaching the DB
Q: "Should you undo someone else's changes/submitted changes?"; A: - probably not
More on undoing needs to be asked to the higher-ups of the course, we are still a bit unsure
Q: "Does javafx need testing?" - Yes, front-end needs testing to the extent that the logic works, key-binding can and should be tested

Announcements by Mike:
Week 8: - Self reflection deadline (formative) - don't go to crazy with it, can be kind of like a draft-ish version
        - Write about the process, code contribution, connect what you've done with the lectures
        - Make it personalized to yourself
Week 9: - Buddycheck deadline (not formative, needs to be done)
        - Code deadline (Friday 23:59)
        - Demo of project pitch - video with commentary over us using the app - Can also be done as a live demo, but we need a back-up video then

Features: - Basic features still missing, try to finalize them before getting to anything else
          - For extra features, they need to be finalized properly to actually count


Presentation of current app to TA: 
- Server selection added upon starting app (pretty cool)
- UI improved for starter and home page
- Creation of event possible, as well as joining it and deleting it (last one is admin only)
- Stats page works


Talking points:
- What have we achieved? - UI greatly improved, testing a bit improved, several more features have been implemented
- UI work - Victor - event overview
            Calin - add expense
            Lucca -  open debts
            Vlad - admin stuff
            Duco - server selection, stats
- Debt splitting algorithm:
   - proposal by vlad: cut link from expense to debt, add a link from event to debt
   - can be hard to work around removing/editing expenses
   - but can make calculating/recalculating debts and achieving the minimum no. of debts
   - useful link for this kind of algorithm (whatsapp) 
   - we decide at a later date
- What's left? 
   - Web sockets and long polling still need to be implemented in the app (for adding/editing events - web sockets; admin - long polling) 
   - Some basic features still need implementing:
          - JSON Dump 
          - Editing/removing various stuff (in the UI, mostly)
          - Debt splitting
          - Confirmation for actions
Work split:
  - All - undoing stuff, finish language (putting all the words), UI improvement
  - Calin - JSON Dump, web sockets
  - Duco - web sockets, more keybinds
  - Lucca - action confirmation, testing
  - Vlad - long polling, debt splitting
  - Victor - finish event overview, connect tags, edit/remove expense
  - Frank - testing, web sockets
  - Whatever else we think of after the meeting
