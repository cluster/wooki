//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.domain.exception;

public class UserAlreadyException extends BusinessException
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 1424814279486457742L;

    public UserAlreadyException()
    {
        super();
    }

    public UserAlreadyException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public UserAlreadyException(String message)
    {
        super(message);
    }

    public UserAlreadyException(Throwable cause)
    {
        super(cause);
    }

}
