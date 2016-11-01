migrate(function () {

  db.features.update(
    {},
    {
      $unset: { status: '' }
    },
    { multi: true }
  );

});
