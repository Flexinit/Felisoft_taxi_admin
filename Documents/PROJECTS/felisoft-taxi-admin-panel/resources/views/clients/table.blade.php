<div class="table-responsive">
    <table class="table" id="clients-table">
        <thead>
        <tr>
            <th>Id</th>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Email</th>
        <th>Email Verified At</th>
        <th>Password</th>
        <th>Phone</th>
        <th>Latitude</th>
        <th>Longitude</th>
        <th>Type</th>
        <th>Profile Picture</th>
        <th>City</th>
        <th>Remember Token</th>
        <th>Country Code</th>
        <th>Average Rating</th>
        <th>Status</th>
        <th>Activation Date</th>
        <th>Driver Type</th>
        <th>Driver Availability Status</th>
        <th>Wallet Balance</th>
        <th>Kin First Name</th>
        <th>Kin Last Name</th>
        <th>Kin Phone</th>
        <th>Kin Email</th>
        <th>Age</th>
        <th>Date Of Birth</th>
        <th>Marital Status</th>
        <th>Address</th>
        <th>Fb Handle</th>
        <th>Twitter Handle</th>
        <th>Insta Handle</th>
        <th>Last Login</th>
        <th>Previous Charge</th>
        <th>Preference Location</th>
        <th>Preference Latitude</th>
        <th>Preference Longitude</th>
        <th>Pool Ride Preference</th>
        <th>Admin Approved</th>
            <th colspan="3">Action</th>
        </tr>
        </thead>
        <tbody>
        @foreach($clients as $clients)
            <tr>
                <td>{{ $clients->id }}</td>
            <td>{{ $clients->first_name }}</td>
            <td>{{ $clients->last_name }}</td>
            <td>{{ $clients->email }}</td>
            <td>{{ $clients->email_verified_at }}</td>
            <td>{{ $clients->password }}</td>
            <td>{{ $clients->phone }}</td>
            <td>{{ $clients->latitude }}</td>
            <td>{{ $clients->longitude }}</td>
            <td>{{ $clients->type }}</td>
            <td>{{ $clients->profile_picture }}</td>
            <td>{{ $clients->city }}</td>
            <td>{{ $clients->remember_token }}</td>
            <td>{{ $clients->country_code }}</td>
            <td>{{ $clients->average_rating }}</td>
            <td>{{ $clients->status }}</td>
            <td>{{ $clients->activation_date }}</td>
            <td>{{ $clients->driver_type }}</td>
            <td>{{ $clients->driver_availability_status }}</td>
            <td>{{ $clients->wallet_balance }}</td>
            <td>{{ $clients->kin_first_name }}</td>
            <td>{{ $clients->kin_last_name }}</td>
            <td>{{ $clients->kin_phone }}</td>
            <td>{{ $clients->kin_email }}</td>
            <td>{{ $clients->age }}</td>
            <td>{{ $clients->date_of_birth }}</td>
            <td>{{ $clients->marital_status }}</td>
            <td>{{ $clients->address }}</td>
            <td>{{ $clients->fb_handle }}</td>
            <td>{{ $clients->twitter_handle }}</td>
            <td>{{ $clients->insta_handle }}</td>
            <td>{{ $clients->last_login }}</td>
            <td>{{ $clients->previous_charge }}</td>
            <td>{{ $clients->preference_location }}</td>
            <td>{{ $clients->preference_latitude }}</td>
            <td>{{ $clients->preference_longitude }}</td>
            <td>{{ $clients->pool_ride_preference }}</td>
            <td>{{ $clients->admin_approved }}</td>
                <td width="120">
                    {!! Form::open(['route' => ['clients.destroy', $clients->id], 'method' => 'delete']) !!}
                    <div class='btn-group'>
                        <a href="{{ route('clients.show', [$clients->id]) }}"
                           class='btn btn-default btn-xs'>
                            <i class="far fa-eye"></i>
                        </a>
                        <a href="{{ route('clients.edit', [$clients->id]) }}"
                           class='btn btn-default btn-xs'>
                            <i class="far fa-edit"></i>
                        </a>
                        {!! Form::button('<i class="far fa-trash-alt"></i>', ['type' => 'submit', 'class' => 'btn btn-danger btn-xs', 'onclick' => "return confirm('Are you sure?')"]) !!}
                    </div>
                    {!! Form::close() !!}
                </td>
            </tr>
        @endforeach
        </tbody>
    </table>
</div>
