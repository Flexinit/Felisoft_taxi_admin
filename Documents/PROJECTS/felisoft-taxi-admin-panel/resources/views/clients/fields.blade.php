<!-- Id Field -->
<div class="form-group col-sm-6">
    {!! Form::label('id', 'Id:') !!}
    {!! Form::number('id', null, ['class' => 'form-control']) !!}
</div>

<!-- First Name Field -->
<div class="form-group col-sm-6">
    {!! Form::label('first_name', 'First Name:') !!}
    {!! Form::text('first_name', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Last Name Field -->
<div class="form-group col-sm-6">
    {!! Form::label('last_name', 'Last Name:') !!}
    {!! Form::text('last_name', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Email Field -->
<div class="form-group col-sm-6">
    {!! Form::label('email', 'Email:') !!}
    {!! Form::email('email', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Email Verified At Field -->
<div class="form-group col-sm-6">
    {!! Form::label('email_verified_at', 'Email Verified At:') !!}
    {!! Form::text('email_verified_at', null, ['class' => 'form-control','id'=>'email_verified_at']) !!}
</div>

@push('page_scripts')
    <script type="text/javascript">
        $('#email_verified_at').datetimepicker({
            format: 'YYYY-MM-DD HH:mm:ss',
            useCurrent: true,
            sideBySide: true
        })
    </script>
@endpush

<!-- Password Field -->
<div class="form-group col-sm-6">
    {!! Form::label('password', 'Password:') !!}
    {!! Form::password('password', ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Phone Field -->
<div class="form-group col-sm-6">
    {!! Form::label('phone', 'Phone:') !!}
    {!! Form::text('phone', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Latitude Field -->
<div class="form-group col-sm-6">
    {!! Form::label('latitude', 'Latitude:') !!}
    {!! Form::text('latitude', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Longitude Field -->
<div class="form-group col-sm-6">
    {!! Form::label('longitude', 'Longitude:') !!}
    {!! Form::text('longitude', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Type Field -->
<div class="form-group col-sm-6">
    <div class="form-check">
        {!! Form::hidden('type', 0, ['class' => 'form-check-input']) !!}
        {!! Form::checkbox('type', '1', null, ['class' => 'form-check-input']) !!}
        {!! Form::label('type', 'Type', ['class' => 'form-check-label']) !!}
    </div>
</div>


<!-- Profile Picture Field -->
<div class="form-group col-sm-6">
    {!! Form::label('profile_picture', 'Profile Picture:') !!}
    {!! Form::text('profile_picture', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- City Field -->
<div class="form-group col-sm-6">
    {!! Form::label('city', 'City:') !!}
    {!! Form::text('city', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Remember Token Field -->
<div class="form-group col-sm-6">
    {!! Form::label('remember_token', 'Remember Token:') !!}
    {!! Form::text('remember_token', null, ['class' => 'form-control','maxlength' => 100,'maxlength' => 100]) !!}
</div>

<!-- Country Code Field -->
<div class="form-group col-sm-6">
    {!! Form::label('country_code', 'Country Code:') !!}
    {!! Form::number('country_code', null, ['class' => 'form-control']) !!}
</div>

<!-- Average Rating Field -->
<div class="form-group col-sm-6">
    {!! Form::label('average_rating', 'Average Rating:') !!}
    {!! Form::number('average_rating', null, ['class' => 'form-control']) !!}
</div>

<!-- Status Field -->
<div class="form-group col-sm-6">
    <div class="form-check">
        {!! Form::hidden('status', 0, ['class' => 'form-check-input']) !!}
        {!! Form::checkbox('status', '1', null, ['class' => 'form-check-input']) !!}
        {!! Form::label('status', 'Status', ['class' => 'form-check-label']) !!}
    </div>
</div>


<!-- Activation Date Field -->
<div class="form-group col-sm-6">
    {!! Form::label('activation_date', 'Activation Date:') !!}
    {!! Form::text('activation_date', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Driver Type Field -->
<div class="form-group col-sm-6">
    <div class="form-check">
        {!! Form::hidden('driver_type', 0, ['class' => 'form-check-input']) !!}
        {!! Form::checkbox('driver_type', '1', null, ['class' => 'form-check-input']) !!}
        {!! Form::label('driver_type', 'Driver Type', ['class' => 'form-check-label']) !!}
    </div>
</div>


<!-- Driver Availability Status Field -->
<div class="form-group col-sm-6">
    <div class="form-check">
        {!! Form::hidden('driver_availability_status', 0, ['class' => 'form-check-input']) !!}
        {!! Form::checkbox('driver_availability_status', '1', null, ['class' => 'form-check-input']) !!}
        {!! Form::label('driver_availability_status', 'Driver Availability Status', ['class' => 'form-check-label']) !!}
    </div>
</div>


<!-- Wallet Balance Field -->
<div class="form-group col-sm-6">
    {!! Form::label('wallet_balance', 'Wallet Balance:') !!}
    {!! Form::number('wallet_balance', null, ['class' => 'form-control']) !!}
</div>

<!-- Kin First Name Field -->
<div class="form-group col-sm-6">
    {!! Form::label('kin_first_name', 'Kin First Name:') !!}
    {!! Form::text('kin_first_name', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Kin Last Name Field -->
<div class="form-group col-sm-6">
    {!! Form::label('kin_last_name', 'Kin Last Name:') !!}
    {!! Form::text('kin_last_name', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Kin Phone Field -->
<div class="form-group col-sm-6">
    {!! Form::label('kin_phone', 'Kin Phone:') !!}
    {!! Form::text('kin_phone', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Kin Email Field -->
<div class="form-group col-sm-6">
    {!! Form::label('kin_email', 'Kin Email:') !!}
    {!! Form::text('kin_email', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Age Field -->
<div class="form-group col-sm-6">
    {!! Form::label('age', 'Age:') !!}
    {!! Form::number('age', null, ['class' => 'form-control']) !!}
</div>

<!-- Date Of Birth Field -->
<div class="form-group col-sm-6">
    {!! Form::label('date_of_birth', 'Date Of Birth:') !!}
    {!! Form::text('date_of_birth', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Marital Status Field -->
<div class="form-group col-sm-6">
    <div class="form-check">
        {!! Form::hidden('marital_status', 0, ['class' => 'form-check-input']) !!}
        {!! Form::checkbox('marital_status', '1', null, ['class' => 'form-check-input']) !!}
        {!! Form::label('marital_status', 'Marital Status', ['class' => 'form-check-label']) !!}
    </div>
</div>


<!-- Address Field -->
<div class="form-group col-sm-6">
    {!! Form::label('address', 'Address:') !!}
    {!! Form::text('address', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Fb Handle Field -->
<div class="form-group col-sm-6">
    {!! Form::label('fb_handle', 'Fb Handle:') !!}
    {!! Form::text('fb_handle', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Twitter Handle Field -->
<div class="form-group col-sm-6">
    {!! Form::label('twitter_handle', 'Twitter Handle:') !!}
    {!! Form::text('twitter_handle', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Insta Handle Field -->
<div class="form-group col-sm-6">
    {!! Form::label('insta_handle', 'Insta Handle:') !!}
    {!! Form::text('insta_handle', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Last Login Field -->
<div class="form-group col-sm-6">
    {!! Form::label('last_login', 'Last Login:') !!}
    {!! Form::text('last_login', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Previous Charge Field -->
<div class="form-group col-sm-6">
    {!! Form::label('previous_charge', 'Previous Charge:') !!}
    {!! Form::number('previous_charge', null, ['class' => 'form-control']) !!}
</div>

<!-- Preference Location Field -->
<div class="form-group col-sm-6">
    {!! Form::label('preference_location', 'Preference Location:') !!}
    {!! Form::text('preference_location', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Preference Latitude Field -->
<div class="form-group col-sm-6">
    {!! Form::label('preference_latitude', 'Preference Latitude:') !!}
    {!! Form::text('preference_latitude', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Preference Longitude Field -->
<div class="form-group col-sm-6">
    {!! Form::label('preference_longitude', 'Preference Longitude:') !!}
    {!! Form::text('preference_longitude', null, ['class' => 'form-control','maxlength' => 191,'maxlength' => 191]) !!}
</div>

<!-- Pool Ride Preference Field -->
<div class="form-group col-sm-6">
    <div class="form-check">
        {!! Form::hidden('pool_ride_preference', 0, ['class' => 'form-check-input']) !!}
        {!! Form::checkbox('pool_ride_preference', '1', null, ['class' => 'form-check-input']) !!}
        {!! Form::label('pool_ride_preference', 'Pool Ride Preference', ['class' => 'form-check-label']) !!}
    </div>
</div>


<!-- Admin Approved Field -->
<div class="form-group col-sm-6">
    <div class="form-check">
        {!! Form::hidden('admin_approved', 0, ['class' => 'form-check-input']) !!}
        {!! Form::checkbox('admin_approved', '1', null, ['class' => 'form-check-input']) !!}
        {!! Form::label('admin_approved', 'Admin Approved', ['class' => 'form-check-label']) !!}
    </div>
</div>
